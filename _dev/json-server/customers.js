module.exports = async (req, res, next) => {  
  const delay = ms => new Promise(resolve => setTimeout(resolve, ms));
  await delay(3000);

  next();
}
