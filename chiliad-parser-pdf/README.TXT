

Mongo help:

Start mongodb:

C:\Users\pk\portable\mongodb-2.6.5\bin>mongod.exe --dbpath C:\Users\pk\Documents\NetBeansProjects\giskard-parser\mongodata\db

mongod --dbpath C:\Users\pk\Documents\NetBeansProjects\CatchupCatExtract\mongodata\db

> db.catalogRaw.find({},{"pages.images.imageBytes" : 0}).pretty()

 db.catalogRaw.find({},{"pages.images.imageBytes" : 0, "pages.tokens": 0}).pretty()


> db.catalogRaw.aggregate({"$project": {"pageNumber": true, "tokens": true}},{"$unwind": "$tokens"},
{"$match": { "tokens.nonStrokingColor": "#009036","tokens.x": {"$gt": "40","$lt": "80"}, "tokens.y":
 { "$gt": "35","$lt": "45"}}}).pretty()



Mongo export:

mongoexport --db schneider --collection acti9_2013 --csv --fields pageNumber,headerLeft,headerRight,productType,pageContent --out ../../output/acti9_2013.csv

mongoexport --db schneider --collection resi9_2013 --csv --fields pageNumber,headerLeft,headerRight,productType,pageContent --out ../../output/resi9_2013.csv