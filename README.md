# Step by step to execute Customer Orders Application that uses Spring MVC

This tutorial was tested on a machine with the Ubuntu 18.04.6 LTS operating system. To follow this tutorial, you need the following tools: `git`, `docker`, `node` and `java 11 or higher`.

>If `git` isn't installed, install it through the [Installing Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) tutorial.

>If `docker` isn't installed, install it through the [Install Docker Engine](https://docs.docker.com/engine/install/) tutorial.

>If `node` isn't installed, install it through the [How to Install Node.js and npm on Ubuntu 18.04](https://linuxize.com/post/how-to-install-node-js-on-ubuntu-18.04) tutorial.

>If `java 11` isn't installed, install it through the [Installing OpenJDK 11 (Java Development Kit) on Ubuntu 18.04](https://www.linode.com/docs/guides/how-to-install-openjdk-on-ubuntu-18-04) tutorial.

To make sure the required tools are installed, run the following commands:
```bash
git version
docker version
node --version
npm --version
java --version
javac --version
```

__1__. If `json-server` has not yet been installed, install it by running the following commands:
```bash
npm install -g json-server && json-server --version
```

__2__. If the __customer-orders-mvc__ repository has not yet been cloned, clone it by running the following commands:
```bash
cd YOUR_PATH
git clone https://github.com/cams7/customer-orders-mvc.git
```

__3__. Initialize mongodb by running the following commands:
```bash
cd YOUR_PATH/customer-orders-mvc
docker run -d --rm --name db -p 27017:27017 -v ${PWD}/_dev/scripts/mongo-init.js:/docker-entrypoint-initdb.d/mongo-init.js:ro mongo:5.0.9
```

__4__. Open a new terminal tab and start a fake app that has the customer data, customer's address data and customer's card data by running the following commands:
```bash
cd YOUR_PATH/customer-orders-mvc
json-server --watch ${PWD}/_dev/json-server/customers.json --port 8084 --host 0.0.0.0 --middlewares ${PWD}/_dev/json-server/customers.js
```

__5__. Open a new terminal tab and start a fake app that has the customer's cart data by running the following commands:
```bash
cd YOUR_PATH/customer-orders-mvc
json-server --watch ${PWD}/_dev/json-server/carts.json --port 8085 --host 0.0.0.0 --middlewares ${PWD}/_dev/json-server/carts.js
```

__6__. Open a new terminal tab and start a fake app used to verify payment by running the following commands:
```bash
cd YOUR_PATH/customer-orders-mvc
java -jar ${PWD}/_dev/payment.jar --server.port=8082
```

__7__. Open a new terminal tab and start a fake app used to create shipping by running the following commands:
```bash
cd YOUR_PATH/customer-orders-mvc
java -jar ${PWD}/_dev/shipping.jar --server.port=8083
```

__8__. Open a new terminal tab and start customer-orders app inside a new docker container by running the following commands:
```bash
cd YOUR_PATH/customer-orders-mvc
docker run -it --rm --net host -v ${PWD}:/work -v ${HOME}/.m2:/root/.m2 -e BUILDER_ADD_CLIENT_CONNECTOR=true -w /work --memory="1g" adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.14.1_1 sh

/work/mvnw clean verify -f /work
java -jar /work/target/customer-orders-mvc-0.0.1-SNAPSHOT.jar --server.port=8080
```

__9__. Open a new terminal tab and run the stress tests inside a new docker container by running the following commands:
```bash
cd YOUR_PATH/customer-orders-mvc
docker run -it --rm --net host -v ${PWD}:/work -w /work alpine:3.16.2 sh

apk update && apk add --no-cache coreutils curl jq tmux wrk

curl 'http://localhost:8084/customers/57a98d98e4b00679b4a830b2' | jq
curl 'http://localhost:8084/addresses?customerId=57a98d98e4b00679b4a830b2&postcode=C1419DVM' | jq
curl 'http://localhost:8084/cards?/customerId=57a98d98e4b00679b4a830b2&longNum=4539820506340218' | jq
curl 'http://localhost:8085/items?customerId=57a98d98e4b00679b4a830b2&cartId=5a934e000102030405000028' | jq

curl 'http://localhost:8080/orders' -H 'country: BR' -H 'requestTraceId: 123BR' | jq
curl 'http://localhost:8080/orders/5a934e000102030405000000' -H 'country: BR' -H 'requestTraceId: 123BR' | jq
curl -X DELETE 'http://localhost:8080/orders/5a934e000102030405000000' -H 'country: BR' -H 'requestTraceId: 123BR'

curl -X POST "http://localhost:8080/orders" -H 'Content-Type: application/json' -H 'country: BR' -H 'requestTraceId: 123BR' -d '{"customerId": "57a98d98e4b00679b4a830b5","addressPostcode": "66625-143","cardNumber": "4929348351581213","cartId": "5a934e000102030405000031"}' | jq

wrk -d1m -t1 -c5 -s /work/_dev/wrk/create-order.lua --latency http://localhost:8080/orders
```

__10__. If you have some MongoDB  Client, after running the stress tests, run the following commands to verify the integrity of the registered data:

	db.getCollection('BR-orders').count({})

	db.getCollection('BR-orders').count({
	  $and:[
	    {'customer.customerId': '57a98d98e4b00679b4a830b5'},
	    {'address.addressId': '57a98d98e4b00679b4a830b3'},
	    {'card.cardId': '57a98d98e4b00679b4a830b4'},
	    {items:{$elemMatch:{productId: '9aff4cc5-f921-4157-8976-41ceae93ae54', "quantity": 1}}},
	    {items:{$elemMatch:{productId: '2bd4204f-26f5-43c0-81c0-ba61230d6131', "quantity": 2}}}
	  ]
	})

	db.getCollection('BR-orders').count({
	  $and:[
	    {'customer.customerId': '5a934e000102030405000004'},
	    {'address.addressId': '5a934e000102030405000005'},
	    {'card.cardId': '5a934e000102030405000006'},
	    {items:{$elemMatch:{productId: 'f7411995-0866-4ebe-b573-4554d275accc', "quantity": 2}}},
	    {items:{$elemMatch:{productId: '416b166c-02ec-49a5-b40f-35db0989ae79', "quantity": 3}}},
	    {items:{$elemMatch:{productId: 'b08dd51c-1af6-4d5d-9b23-91ffdabdb18a', "quantity": 3}}},
	    {items:{$elemMatch:{productId: '0be50778-8fed-4085-8e26-45f52e97b507', "quantity": 2}}},
	    {items:{$elemMatch:{productId: '0ad23a9d-88fc-4924-b282-487c2eb60e64', "quantity": 1}}}
	  ]
	})

	db.getCollection('BR-orders').count({
	  $and:[
	    {'customer.customerId': '5a934e000102030405000007'},
	    {'address.addressId': '5a934e000102030405000008'},
	    {'card.cardId': '5a934e000102030405000009'},
	    {items:{$elemMatch:{productId: 'f7411995-0866-4ebe-b573-4554d275accc', "quantity": 5}}},
	    {items:{$elemMatch:{productId: 'b08dd51c-1af6-4d5d-9b23-91ffdabdb18a', "quantity": 6}}},
	    {items:{$elemMatch:{productId: '0ad23a9d-88fc-4924-b282-487c2eb60e64', "quantity": 3}}},
	    {items:{$elemMatch:{productId: '9349ff7d-0856-438b-95ca-5b7ab35112fe', "quantity": 2}}}
	  ]
	})

	db.getCollection('BR-orders').count({'registeredShipping': {$ne: true}})

	db.getCollection('shippings').count({country:'BR'})
