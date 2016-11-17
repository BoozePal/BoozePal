insert into boozepal_drinktype(id,name) values(1,'beer');
insert into boozepal_drinktype(id,name) values(2,'vodka');
insert into boozepal_drinktype(id,name) values(3,'rum');

insert into boozepal_drink(id,name,drinkType_id) values(1,'Borsodi Sör',1);
insert into boozepal_drink(id,name,drinkType_id) values(2,'Sör',1);
insert into boozepal_drink(id,name,drinkType_id) values(3,'Habos sör',1);
insert into boozepal_drink(id,name,drinkType_id) values(4,'Royal vodka',2);
insert into boozepal_drink(id,name,drinkType_id) values(5,'Gyengébb rum',3);
insert into boozepal_drink(id,name,drinkType_id) values(6,'Közepes rum',3);
insert into boozepal_drink(id,name,drinkType_id) values(7,'Erős rum',3);

insert into boozepal_user(id,username,password,remove,loggedin) values(10,'DrinkTestUser','XXX',false,false);

insert into boozepal_user_boozepal_drink(boozepal_user_id,favouriteDrinks_id) values(10,1);
insert into boozepal_user_boozepal_drink(boozepal_user_id,favouriteDrinks_id) values(10,2);

