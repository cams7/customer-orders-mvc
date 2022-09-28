const OK = 200;
const BAD_REQUEST = 400;
const NOT_ACCEPTABLE = 406;
const INTERNAL_SERVER_ERROR = 500;

module.exports = async (req, res, next) => {
  const path = req._parsedUrl.path;

  switch (req.method) {
    case 'POST':
      if(path.match(/^\/payments$/)) {
        const delay = ms => new Promise(resolve => setTimeout(resolve, ms));
        await delay(10000);
        verifyPayment(req, res, path); 
      }
      break;
    case 'GET':
      break;
    case 'DELETE':
      break;
    case 'PATCH' :
      break;
    default:
      break;
  }

  next();
}

function verifyPayment(req, res, path) {
  sendResponse(req, res, path, (send, req, res, path, responseBody) => {
    const payment = getJsonData(responseBody);

    if(payment && payment.customerId && payment.amount) {
      const declineOverAmount = 100.0;
      
      res.status(OK);
      if(payment.amount <= declineOverAmount) {        
        send.call(res, JSON.stringify({authorised: true, message: 'Payment authorised'}));
      } else {
        send.call(res, JSON.stringify({authorised: false, message: `Payment declined: amount exceeds ${declineOverAmount.toFixed(2)}`}));  
      }      
    } else {
      sendError(res, path, send, "The request body isn't valid", BAD_REQUEST);
    }
  });
}

function sendResponse(req, res, path, callback) {
  const send = res.send;
  res.send = function (responseBody) {
    try {
       callback(send, req, this, path, responseBody);
    } catch (err) {
      sendError(this, path, send, err, INTERNAL_SERVER_ERROR);
    }
  };
}

function sendError(res, path, send, errorMessage, statusCode) {
  res.status(statusCode);
  send.call(res, getError(errorMessage, statusCode, path));
}

function getError(errorMessage, statusCode, path) {
  return `{"timestamp": "${getTimestamp()}", "status": ${statusCode}, "error": "${getStatusName(statusCode)}", "message": "${errorMessage}", "path": "${path}"}`;
}

function getStatusName(statusCode) {
  if(statusCode === BAD_REQUEST)
    return  "Bad Request";
 
  if(statusCode === NOT_ACCEPTABLE)
    return  "Not Acceptable";

  if(statusCode === INTERNAL_SERVER_ERROR)
    return  "Internal Server Error";

  return null;
}

function getTimestamp() {
  return new Date().toISOString().replace(/Z$/, "+00:00");	
}

function getJsonData (value) {
  if(!value)
    return null;

  if(typeof value === 'string') 
    return JSON.parse(value);

  return value;
}
