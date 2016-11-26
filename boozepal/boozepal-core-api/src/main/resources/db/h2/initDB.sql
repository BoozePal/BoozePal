/*sör*/
insert into boozepal_drinktype(id,name,displayName) values(1,'beer','Sör'); 
/*vodka*/
insert into boozepal_drinktype(id,name,displayName) values(2,'vodka','Vodka');
/*rum*/
insert into boozepal_drinktype(id,name,displayName) values(3,'rum','Rum');

insert into boozepal_drink(id,name,drinkType_id) values(1,'Borsodi',1);
insert into boozepal_drink(id,name,drinkType_id) values(2,'Sör',1);
insert into boozepal_drink(id,name,drinkType_id) values(3,'Habos sör',1);
insert into boozepal_drink(id,name,drinkType_id) values(4,'Royal vodka',2);
insert into boozepal_drink(id,name,drinkType_id) values(5,'Gyengébb rum',3);
insert into boozepal_drink(id,name,drinkType_id) values(6,'Közepes rum',3);
insert into boozepal_drink(id,name,drinkType_id) values(7,'Eros rum',3);

insert into boozepal_user(id,username,password,remove,loggedin,searchradius,pricecategory) values(10,'DrinkTestUser','XXX',false,false,1,1);
insert into boozepal_user(id,username,password,remove,loggedin,searchradius,pricecategory) values(11,'DrinkTestUser2','XXX',false,false,1,3);

insert into boozepal_role (id,roleName) values(10,'ROLE_USER');

insert into boozepal_user_boozepal_role(User_id,role_id) values(10,(select id from boozepal_role where roleName = 'ROLE_USER'));
insert into boozepal_user_boozepal_role(User_id,role_id) values(11,(select id from boozepal_role where roleName = 'ROLE_USER'));
/*sör*/
insert into boozepal_user_boozepal_drink(boozepal_user_id,drink_id) values(10,1);
/*sör*/
insert into boozepal_user_boozepal_drink(boozepal_user_id,drink_id) values(10,2);
/*rum*/
insert into boozepal_user_boozepal_drink(boozepal_user_id,drink_id) values(10,7);
/*rum*/
insert into boozepal_user_boozepal_drink(boozepal_user_id,drink_id) values(11,7);
/*rum*/
insert into boozepal_user_boozepal_drink(boozepal_user_id,drink_id) values(11,6);

