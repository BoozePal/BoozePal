
insert into boozepal_drink(id,name,drinkType) values(1,'Borsodi','BEER');
insert into boozepal_drink(id,name,drinkType) values(2,'Sör','BEER');
insert into boozepal_drink(id,name,drinkType) values(3,'Habos sör','BEER');
insert into boozepal_drink(id,name,drinkType) values(4,'Royal vodka','VODKA');
insert into boozepal_drink(id,name,drinkType) values(5,'Gyengébb rum','RUM');
insert into boozepal_drink(id,name,drinkType) values(6,'Közepes rum','RUM');
insert into boozepal_drink(id,name,drinkType) values(7,'Eros rum','RUM');

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

