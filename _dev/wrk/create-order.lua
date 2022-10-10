country = "BR"

customerUrl = "http://localhost:8084"
cartUrl = "http://localhost:8085"

customerIds = {
   "57a98d98e4b00679b4a830b5",
   "5a934e000102030405000004",
   "5a934e000102030405000007"
}
addressIds = {
   "57a98d98e4b00679b4a830b3",
   "5a934e000102030405000005",
   "5a934e000102030405000008"
}
cardIds = {
   "57a98d98e4b00679b4a830b4",
   "5a934e000102030405000006",
   "5a934e000102030405000009"
}
cartIds = {
   "5a934e000102030405000031",
   "5a934e000102030405000037",
   "5a934e000102030405000043"
}

math.randomseed(os.clock()*100000000000)

getCustomerUrl = function(customerId)
    return customerUrl.."/customers/"..customerId
end

getAddressUrl = function(addressId)
    return customerUrl.."/addresses/"..addressId
end

getCardUrl = function(cardId)
    return customerUrl.."/cards/"..cardId
end

getItemsUrl = function(cartId)
    return cartUrl.."/carts/"..cartId.."/items"
end

getBody = function()
    index = math.random(1, 3)
    return "{\"customerUrl\": \""..getCustomerUrl(customerIds[index]).."\",\"addressUrl\": \""..getAddressUrl(addressIds[index]).."\",\"cardUrl\": \""..getCardUrl(cardIds[index]).."\",\"itemsUrl\": \""..getItemsUrl(cartIds[index]).."\"}"
end

getRequestTraceId = function()
    return string.lower(country).."-"..math.random(10000, 65000)
end

wrk.method = "POST"
wrk.body = getBody()
wrk.headers["Content-Type"] = "application/json"
wrk.headers["country"] = country
wrk.headers["requestTraceId"] = getRequestTraceId()

