# Order formation system


## Functions
___
This project implements an order formation system that allows the user to specify a product and a customer to create an order. The system checks the presence of the specified product in the warehouse and the presence of the buyer in the list of buyers.

## Input data
___
The service accepts a request with a Body containing the following data:

Buyer

- [ ] Name (Name) - a string indicating the name of the buyer.
- [ ] Email - a string indicating the buyer's e-mail address.
- [ ] Phone - a string indicating the customer's phone number.

Product

- [ ] Name - a string indicating the name of the item.
- [ ] Quantity - a numeric value indicating the desired quantity of the product.
- [ ] Cost - a numeric value indicating the cost of the item.
- [ ] Amount - a numeric value indicating the amount of goods.

## Output
___
At the output, an Order is generated containing the following data:

- [ ] Amount - a string indicating the quantity of goods in the order.
- [ ] Product - a string indicating the type of product in the order.
- [ ] Customer (Customer) - a string indicating the customer.

## Operation logic
___
The service receives a request with data about the buyer and the product.
The presence of the specified product in the list of products in the warehouse is checked.
Checks whether the specified buyer is in the list of buyers.
If the goods and the buyer are present in the respective lists, the order is considered to be successfully formed.
Otherwise, an error is returned indicating that there is no product or customer.

## Are used

___
- Postgre SQL
- Spring Boot
- Swagger
- Docker
- TestContainers




