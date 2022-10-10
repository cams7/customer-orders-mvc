users = db.getSiblingDB("users");
users.createCollection("customers");
users.createCollection("addresses");
users.createCollection("cards");

carts = db.getSiblingDB("carts");
carts.createCollection("carts");
carts.createCollection("items");

orders = db.getSiblingDB("customer-orders");
orders.createCollection("BR-orders");

users.customers.insertMany([
    {
        "_id": ObjectId("57a98d98e4b00679b4a830af"),
        "firstName": "Gustavo",
        "lastName": "Ramos",
        "username": "gustavo",
        "password": "e2de7202bb2201842d041f6de201b10438369fb8",
        "salt": "6c1c6176e8b455ef37da13d953df971c249d0d8e",
        "addresses": [ObjectId("57a98d98e4b00679b4a830ad")],
        "cards": [ObjectId("57a98d98e4b00679b4a830ae")]
    },
    {
        "_id": ObjectId("57a98d98e4b00679b4a830b2"),
        "firstName": "Juan",
        "lastName": "Tercero",
        "username": "juan",
        "password": "e2de7202bb2201842d041f6de201b10438369fb8",
        "salt": "6c1c6176e8b455ef37da13d953df971c249d0d8e",
        "addresses": [ObjectId("57a98d98e4b00679b4a830b0")],
        "cards": [ObjectId("57a98d98e4b00679b4a830b1")]
    },
    {
        "_id": ObjectId("57a98d98e4b00679b4a830b5"),
        "firstName": "Raimundo",
        "lastName": "Araújo",
        "username": "raimundo",
        "password": "e2de7202bb2201842d041f6de201b10438369fb8",
        "salt": "6c1c6176e8b455ef37da13d953df971c249d0d8e",
        "addresses": [ObjectId("57a98d98e4b00679b4a830b3")],
        "cards": [ObjectId("57a98d98e4b00679b4a830b4")]
    },
    {
        "_id": ObjectId("5a934e000102030405000003"),
        "firstName": "Santino",
        "lastName": "Colunga",
        "username": "santino",
        "password": "e2de7202bb2201842d041f6de201b10438369fb8",
        "salt": "6c1c6176e8b455ef37da13d953df971c249d0d8e",
        "addresses": [ObjectId("57a98ddce4b00679b4a830d1")],
        "cards": [ObjectId("57a98ddce4b00679b4a830d2")]
    },
    {
        "_id": ObjectId("5a934e000102030405000004"),
        "firstName": "Martin",
        "lastName": "Rocha",
        "username": "martin",
        "password": "e2de7202bb2201842d041f6de201b10438369fb8",
        "salt": "6c1c6176e8b455ef37da13d953df971c249d0d8e",
        "addresses": [ObjectId("5a934e000102030405000005")],
        "cards": [ObjectId("5a934e000102030405000006")]
    },
    {
        "_id": ObjectId("5a934e000102030405000007"),
        "firstName": "Luzia",
        "lastName": "Mendes",
        "username": "luzia",
        "password": "e2de7202bb2201842d041f6de201b10438369fb8",
        "salt": "6c1c6176e8b455ef37da13d953df971c249d0d8e",
        "addresses": [ObjectId("5a934e000102030405000008")],
        "cards": [ObjectId("5a934e000102030405000009")]
    },
    {
        "_id": ObjectId("5a934e000102030405000010"),
        "firstName": "Sebastián",
        "lastName": "Longoria",
        "username": "sebastian",
        "password": "e2de7202bb2201842d041f6de201b10438369fb8",
        "salt": "6c1c6176e8b455ef37da13d953df971c249d0d8e",
        "addresses": [ObjectId("5a934e000102030405000011")],
        "cards": [ObjectId("5a934e000102030405000012")]
    }
]);
users.addresses.insertMany([
    {
        "_id": ObjectId("57a98d98e4b00679b4a830ad"),
        "number": "956",
        "street": "Rua Manoel Gregório Mattos",
        "city": "Chapecó",
        "postcode": "89816-170",
        "country": "BR",
        "federativeUnit": "SC"
    },
    {
        "_id": ObjectId("57a98d98e4b00679b4a830b0"),
        "number": "2565",
        "street": "Av Albarellos",
        "city": "Buenos Aires",
        "postcode": "C1419DVM",
        "country": "AR",
        "federativeUnit": "Ciudad Autónoma de Buenos Aires"
    },
    {
        "_id": ObjectId("57a98d98e4b00679b4a830b3"),
        "number": "773",
        "street": "Passagem Santa Maria",
        "city": "Belém",
        "postcode": "66625-143",
        "country": "BR",
        "federativeUnit": "PA"
    },
    {
        "_id": ObjectId("57a98ddce4b00679b4a830d1"),
        "number": "1025",
        "street": "San Fernando del Valle de Catamarca",
        "city": "Municipio de San Fernando del Valle de Catamarca",
        "postcode": "4700",
        "country": "AR",
        "federativeUnit": "Catamarca"
    },
    {
        "_id": ObjectId("5a934e000102030405000005"),
        "number": "404",
        "street": "Rua Orlando Ferreira",
        "city": "Navegantes",
        "postcode": "88371530",
        "country": "BR",
        "federativeUnit": "SC"
    },
    {
        "_id": ObjectId("5a934e000102030405000008"),
        "number": "568",
        "street": "Rua de 15 Novembro",
        "city": "Codajás",
        "postcode": "69450970",
        "country": "BR",
        "federativeUnit": "AM"
    },
    {
        "_id": ObjectId("5a934e000102030405000011"),
        "number": "455",
        "street": "Av Italia",
        "city": "Resistencia",
        "postcode": "H3500ALD",
        "country": "AR",
        "federativeUnit": "Chaco"
    }
]);
users.cards.insertMany([
    {
        "_id": ObjectId("57a98d98e4b00679b4a830ae"),
        "longNum": "5413096279109654",
        "expires": "04/2023",
        "ccv": "320"
    },
    {
        "_id": ObjectId("57a98d98e4b00679b4a830b1"),
        "longNum": "4539820506340218",
        "expires": "02/2027",
        "ccv": "668"
    },
    {
        "_id": ObjectId("57a98d98e4b00679b4a830b4"),
        "longNum": "4929348351581213",
        "expires": "06/2024",
        "ccv": "441"
    },
    {
        "_id": ObjectId("57a98ddce4b00679b4a830d2"),
        "longNum": "374301540174281",
        "expires": "11/2028",
        "ccv": "275"
    },
    {
        "_id": ObjectId("5a934e000102030405000006"),
        "longNum": "5347243146720427",
        "expires": "03/2023",
        "ccv": "292"
    },
    {
        "_id": ObjectId("5a934e000102030405000009"),
        "longNum": "5598962318326244",
        "expires": "12/2023",
        "ccv": "967"
    },
    {
        "_id": ObjectId("5a934e000102030405000012"),
        "longNum": "6011789971225778",
        "expires": "08/2027",
        "ccv": "988"
    }
]);
carts.carts.insertMany([
    {
        "_id": ObjectId("5a934e000102030405000013"),
        "customerId": "57a98d98e4b00679b4a830af",
        "items": [ObjectId("5a934e000102030405000014"), ObjectId("5a934e000102030405000015"), ObjectId("5a934e000102030405000016")]
    },
    {
        "_id": ObjectId("5a934e000102030405000017"),
        "customerId": "57a98d98e4b00679b4a830b2",
        "items": [ObjectId("5a934e000102030405000018"), ObjectId("5a934e000102030405000019"), ObjectId("5a934e000102030405000020")]
    },
    {
        "_id": ObjectId("5a934e000102030405000021"),
        "customerId": "57a98d98e4b00679b4a830af",
        "items": [ObjectId("5a934e000102030405000022"), ObjectId("5a934e000102030405000023")]
    },

    {
        "_id": ObjectId("5a934e000102030405000024"),
        "customerId": "57a98d98e4b00679b4a830af",
        "items": [ObjectId("5a934e000102030405000025"), ObjectId("5a934e000102030405000026"), ObjectId("5a934e000102030405000027")]
    },
    {
      "_id": ObjectId("5a934e000102030405000028"),
      "customerId": "57a98d98e4b00679b4a830b2",
      "items": [ObjectId("5a934e000102030405000029"), ObjectId("5a934e000102030405000030")]
    },
    {
      "_id": ObjectId("5a934e000102030405000031"),
      "customerId": "57a98d98e4b00679b4a830b5",
      "items": [ObjectId("5a934e000102030405000032"), ObjectId("5a934e000102030405000033")]
    },
    {
      "_id": ObjectId("5a934e000102030405000034"),
      "customerId": "5a934e000102030405000003",
      "items": [ObjectId("5a934e000102030405000035"), ObjectId("5a934e000102030405000036")]
    },
    {
      "_id": ObjectId("5a934e000102030405000037"),
      "customerId": "5a934e000102030405000004",
      "items": [ObjectId("5a934e000102030405000038"), ObjectId("5a934e000102030405000039"), ObjectId("5a934e000102030405000040"), ObjectId("5a934e000102030405000041"),ObjectId("5a934e000102030405000042")]
    },
    {
      "_id": ObjectId("5a934e000102030405000043"),
      "customerId": "5a934e000102030405000007",
      "items": [ObjectId("5a934e000102030405000044"), ObjectId("5a934e000102030405000045"), ObjectId("5a934e000102030405000046"), ObjectId("5a934e000102030405000047")]
    },
    {
      "_id": ObjectId("5a934e000102030405000048"),
      "customerId": "5a934e000102030405000010",
      "items": [ObjectId("5a934e000102030405000049"), ObjectId("5a934e000102030405000050"), ObjectId("5a934e000102030405000051"), ObjectId("5a934e000102030405000052"), ObjectId("5a934e000102030405000053")]
    }
]);
carts.items.insertMany([
    {   
        "_id": ObjectId("5a934e000102030405000014"),
        "cartId": "5a934e000102030405000013",
        "productId": "9aff4cc5-f921-4157-8976-41ceae93ae54",
        "quantity": 1,
        "unitPrice": 18.0
    },
    {
        "_id": ObjectId("5a934e000102030405000015"),
        "cartId": "5a934e000102030405000013",
        "productId": "e67ef6e3-10f5-42dd-8b98-dd0d793ca2fa",
        "quantity": 1,
        "unitPrice": 35.0
    },
    {
        "_id": ObjectId("5a934e000102030405000016"),
        "cartId": "5a934e000102030405000013",
        "productId": "2c22ea64-39f1-474b-92d2-b92684dedaa0",
        "quantity": 1,
        "unitPrice": 27.0
    },
    {
        "_id": ObjectId("5a934e000102030405000018"),
        "cartId": "5a934e000102030405000017",
        "productId": "f4d8d296-3ac8-4073-beef-c703cc5030f2",
        "quantity": 2,
        "unitPrice": 9.0
    },
    {
        "_id": ObjectId("5a934e000102030405000019"),
        "cartId": "5a934e000102030405000017",
        "productId": "6799d4e5-23aa-4da2-b10e-4a09ee053ceb",
        "quantity": 3,
        "unitPrice": 11.5
    },
    {
        "_id": ObjectId("5a934e000102030405000020"),
        "cartId": "5a934e000102030405000017",
        "productId": "2c22ea64-39f1-474b-92d2-b92684dedaa0",
        "quantity": 1,
        "unitPrice": 27.0
    },
    {
        "_id": ObjectId("5a934e000102030405000022"),
        "cartId": "5a934e000102030405000021",
        "productId": "9aff4cc5-f921-4157-8976-41ceae93ae54",
        "quantity": 1,
        "unitPrice": 18.0
    },
    {
        "_id": ObjectId("5a934e000102030405000023"),
        "cartId": "5a934e000102030405000021",
        "productId": "2bd4204f-26f5-43c0-81c0-ba61230d6131",
        "quantity": 2,
        "unitPrice": 21.0
    },

    {
        "_id": ObjectId("5a934e000102030405000025"),
        "productId": "9aff4cc5-f921-4157-8976-41ceae93ae54",
        "quantity": 3,
        "unitPrice": 18.0,
        "cartId": "5a934e000102030405000024"
    },
    {
        "_id": ObjectId("5a934e000102030405000026"),
        "productId": "e67ef6e3-10f5-42dd-8b98-dd0d793ca2fa",
        "quantity": 2,
        "unitPrice": 35.0,
        "cartId": "5a934e000102030405000024"
    },
    {
        "_id": ObjectId("5a934e000102030405000027"),
        "productId": "2c22ea64-39f1-474b-92d2-b92684dedaa0",
        "quantity": 1,
        "unitPrice": 27.0,
        "cartId": "5a934e000102030405000024"
    },
    {
        "_id": ObjectId("5a934e000102030405000029"),
        "productId": "9aff4cc5-f921-4157-8976-41ceae93ae54",
        "quantity": 1,
        "unitPrice": 18.0,
        "cartId": "5a934e000102030405000028"
    },
    {
        "_id": ObjectId("5a934e000102030405000030"),
        "productId": "2bd4204f-26f5-43c0-81c0-ba61230d6131",
        "quantity": 2,
        "unitPrice": 21.0,
        "cartId": "5a934e000102030405000028"
    },
    {
        "_id": ObjectId("5a934e000102030405000032"),
        "productId": "9aff4cc5-f921-4157-8976-41ceae93ae54",
        "quantity": 1,
        "unitPrice": 18.0,
        "cartId": "5a934e000102030405000031"
    },
    {
        "_id": ObjectId("5a934e000102030405000033"),
        "productId": "2bd4204f-26f5-43c0-81c0-ba61230d6131",
        "quantity": 2,
        "unitPrice": 21.0,
        "cartId": "5a934e000102030405000031"
    },
    {
        "_id": ObjectId("5a934e000102030405000035"),
        "productId": "d610629a-e8d8-4d84-b87e-ef518caa66bd",
        "quantity": 2,
        "unitPrice": 9.5,
        "cartId": "5a934e000102030405000034"
    },
    {
        "_id": ObjectId("5a934e000102030405000036"),
        "productId": "1cd7aef5-de02-44e0-a6d3-633799c91964",
        "quantity": 3,
        "unitPrice": 14.0,
        "cartId": "5a934e000102030405000034"
    },
    {
        "_id": ObjectId("5a934e000102030405000038"),
        "productId": "f7411995-0866-4ebe-b573-4554d275accc",
        "quantity": 2,
        "unitPrice": 5.5,
        "cartId": "5a934e000102030405000037"
    },
    {
        "_id": ObjectId("5a934e000102030405000039"),
        "productId": "416b166c-02ec-49a5-b40f-35db0989ae79",
        "quantity": 3,
        "unitPrice": 11.5,
        "cartId": "5a934e000102030405000037"
    },
    {
        "_id": ObjectId("5a934e000102030405000040"),
        "productId": "b08dd51c-1af6-4d5d-9b23-91ffdabdb18a",
        "quantity": 3,
        "unitPrice": 3.5,
        "cartId": "5a934e000102030405000037"
    },
    {
        "_id": ObjectId("5a934e000102030405000041"),
        "productId": "0be50778-8fed-4085-8e26-45f52e97b507",
        "quantity": 2,
        "unitPrice": 13.5,
        "cartId": "5a934e000102030405000037"
    },
    {
        "_id": ObjectId("5a934e000102030405000042"),
        "productId": "0ad23a9d-88fc-4924-b282-487c2eb60e64",
        "quantity": 1,
        "unitPrice": 2.5,
        "cartId": "5a934e000102030405000037"
    },
    {
        "_id": ObjectId("5a934e000102030405000044"),
        "productId": "f7411995-0866-4ebe-b573-4554d275accc",
        "quantity": 5,
        "unitPrice": 5.5,
        "cartId": "5a934e000102030405000043"
    },
    {
        "_id": ObjectId("5a934e000102030405000045"),
        "productId": "b08dd51c-1af6-4d5d-9b23-91ffdabdb18a",
        "quantity": 6,
        "unitPrice": 3.5,
        "cartId": "5a934e000102030405000043"
    },
    {
        "_id": ObjectId("5a934e000102030405000046"),
        "productId": "0ad23a9d-88fc-4924-b282-487c2eb60e64",
        "quantity": 3,
        "unitPrice": 2.5,
        "cartId": "5a934e000102030405000043"
    },
    {
        "_id": ObjectId("5a934e000102030405000047"),
        "productId": "9349ff7d-0856-438b-95ca-5b7ab35112fe",
        "quantity": 2,
        "unitPrice": 12.7,
        "cartId": "5a934e000102030405000043"
    },
    {
        "_id": ObjectId("5a934e000102030405000049"),
        "productId": "1b22252e-4291-4501-8908-c5796f1b1d45",
        "quantity": 1,
        "unitPrice": 17.7,
        "cartId": "5a934e000102030405000048"
    },
    {
        "_id": ObjectId("5a934e000102030405000050"),
        "productId": "d197efe1-4d0b-40d2-add1-a3a9803e3cee",
        "quantity": 1,
        "unitPrice": 14.7,
        "cartId": "5a934e000102030405000048"
    },
    {
        "_id": ObjectId("5a934e000102030405000051"),
        "productId": "170c85c8-e4d3-4c55-8c80-9aa7b46d2eb1",
        "quantity": 4,
        "unitPrice": 3.7,
        "cartId": "5a934e000102030405000048"
    },
    {
        "_id": ObjectId("5a934e000102030405000052"),
        "productId": "2dfd5320-6555-4a90-acf3-dd3fe2617040",
        "quantity": 2,
        "unitPrice": 6.7,
        "cartId": "5a934e000102030405000048"
    },
    {
        "_id": ObjectId("5a934e000102030405000053"),
        "productId": "66bd0146-ddcf-4b85-8e23-5b181d32ee6c",
        "quantity": 3,
        "unitPrice": 8.7,
        "cartId": "5a934e000102030405000048"
    }
]);
orders['BR-orders'].insertMany([
   {
      "_id": ObjectId("5a934e000102030405000000"),
      "customer": {
         "customerId": "57a98d98e4b00679b4a830af",
         "fullName": "Gustavo Ramos",
         "username": "gustavo"
      },
      "address": {
        "addressId": "57a98d98e4b00679b4a830ad",
        "number": "956",
        "street": "Rua Manoel Gregório Mattos",
        "postcode": "89816-170",
        "city": "Chapecó",        
        "federativeUnit": "SC",
        "country": "BR"
      },
      "card": {
        "cardId": "57a98d98e4b00679b4a830ae",
        "longNum": "5413096279109654"
      },
      "items": [
        {
          "productId": "9aff4cc5-f921-4157-8976-41ceae93ae54",
          "quantity": 1,
          "unitPrice": 18.0
        },
        {
          "productId": "e67ef6e3-10f5-42dd-8b98-dd0d793ca2fa",
          "quantity": 1,
          "unitPrice": 35.0
        },
        {
          "productId": "2c22ea64-39f1-474b-92d2-b92684dedaa0",
          "quantity": 1,
          "unitPrice": 27.0
        }
      ],
      "registrationDate": ISODate("2022-09-22T17:46:53Z"),
      "total": 90.55,
      "registeredShipping": true
    },
    {
      "_id": ObjectId("5a934e000102030405000001"),
      "customer": {
         "customerId": "57a98d98e4b00679b4a830b2",
         "fullName": "Juan Tercero",
         "username": "juan"
      },
      "address": {
        "addressId": "57a98d98e4b00679b4a830b0", 
        "number": "2565",
        "street": "Av Albarellos",
        "postcode": "C1419DVM",
        "city": "Buenos Aires",
        "federativeUnit": "Ciudad Autónoma de Buenos Aires",       
        "country": "AR"        
      },
      "card": {
        "cardId": "57a98d98e4b00679b4a830b1",
        "longNum": "4539820506340218"
      },
      "items": [
        {
          "productId": "f4d8d296-3ac8-4073-beef-c703cc5030f2",
          "quantity": 2,
          "unitPrice": 9.0
        },
        {
          "productId": "6799d4e5-23aa-4da2-b10e-4a09ee053ceb",
          "quantity": 3,
          "unitPrice": 11.5
        },
        {
          "productId": "2c22ea64-39f1-474b-92d2-b92684dedaa0",
          "quantity": 1,
          "unitPrice": 27.0
        }
      ],
      "registrationDate": ISODate("2022-09-21T12:30:21Z"),
      "total": 90.05,
      "registeredShipping": true
    },
    {
      "_id": ObjectId("5a934e000102030405000002"),
      "customer": {
         "customerId": "57a98d98e4b00679b4a830af",
         "fullName": "Gustavo Ramos",
         "username": "gustavo"
      },
      "address": {
        "addressId": "57a98d98e4b00679b4a830ad",
        "number": "956",
        "street": "Rua Manoel Gregório Mattos",
        "postcode": "89816-170",
        "city": "Chapecó",        
        "federativeUnit": "SC",
        "country": "BR"
      },
      "card": {
        "cardId": "57a98d98e4b00679b4a830ae",
        "longNum": "5413096279109654"
      },
      "items": [
        {
          "productId": "9aff4cc5-f921-4157-8976-41ceae93ae54",
          "quantity": 1,
          "unitPrice": 18.0
        },
        {
          "productId": "2bd4204f-26f5-43c0-81c0-ba61230d6131",
          "quantity": 2,
          "unitPrice": 21.0
        }
      ],
      "registrationDate": ISODate("2022-09-21T16:21:44Z"),
      "total": 70.55,
      "registeredShipping": true
    }
]);
