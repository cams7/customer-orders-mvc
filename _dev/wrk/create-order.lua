country = "BR"

customerUrl = "http://localhost:3000"
cartUrl = "http://localhost:3001"

customerIds = {
   "7329d57a-4028-41cc-9626-a0c41246a623"
}
addressIds = {
   "594fbca6-270c-4299-8b2c-084da56c756c"
}
cardIds = {
   "cfecdbd8-6dd1-45ae-a453-1adc20a6b065"
}
cartIds = {
   "ca36c27b-d9f9-457a-be0b-2aab214cda65"
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
    index = 1
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

