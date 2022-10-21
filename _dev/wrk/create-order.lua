-- wrk -c 500 -t 10 -d 30s -s ./create-order.lua --latency http://localhost:8080/orders

country = "BR"

customerIds = {
   "57a98d98e4b00679b4a830b5",
   "5a934e000102030405000004",
   "5a934e000102030405000007"
}
addressPostcodes = {
    "66625-143",
    "88371530",
    "69450970"
}
cardNumbers = {
    "4929348351581213",
    "5347243146720427",
    "5598962318326244"
}
cartIds = {
   "5a934e000102030405000031",
   "5a934e000102030405000037",
   "5a934e000102030405000043"
}

getBody = function(index)
    return "{\"customerId\": \""..customerIds[index].."\",\"addressPostcode\": \""..addressPostcodes[index].."\",\"cardNumber\": \""..cardNumbers[index].."\",\"cartId\": \""..cartIds[index].."\"}"
end

getRequestTraceId = function()
    return string.lower(country).."-"..(os.clock()*100000000000)
end

-- init random
setup = function(thread)
  math.randomseed(os.time())
end

request = function()
 local path = "/orders"
 local body = getBody(math.random(1, 3))
 wrk.method = "POST"
 wrk.headers["Content-Type"] = "application/json"
 wrk.headers["country"] = country
 wrk.headers["requestTraceId"] = getRequestTraceId()

 return wrk.format("POST", path, wrk.headers, body)
end

done = function(summary, latency, requests)
 io.write("------------------------------\n")
 for _, p in pairs({50, 90, 99, 99.999}) do
  n = latency:percentile(p)
  io.write(string.format("%g%%,%d\n", p, n))
 end
end