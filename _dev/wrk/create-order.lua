country = "BR"

customerUrl = "http://localhost:3000"
cartUrl = "http://localhost:3001"

customerIds = {
   "7329d57a-4028-41cc-9626-a0c41246a623",
   "118e7d8a-d3fd-493a-a759-caeadf8c775e",
   "b2322673-3bfa-4549-96ac-55fe7dd1a02d"
}
addressIds = {
   "594fbca6-270c-4299-8b2c-084da56c756c",
   "a8456b71-f779-4e8b-b291-7fa5b3617ede",
   "0eae20ce-dc86-48f2-b616-8495c38b2386"
}
cardIds = {
   "cfecdbd8-6dd1-45ae-a453-1adc20a6b065",
   "c0632d7c-9e8d-423d-978a-3302fd93decf",
   "e76032b1-09ea-4974-9f83-92a6ee216972"
}
cartIds = {
   "ca36c27b-d9f9-457a-be0b-2aab214cda65",
   "214b5385-2452-4f53-b6ee-f0fa318deaa2",
   "fdda1a8a-f374-4346-a112-561c6e93ccb4"
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

