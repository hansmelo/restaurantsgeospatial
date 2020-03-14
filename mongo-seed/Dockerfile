FROM mongo

COPY restaurants.json /restaurants.json
COPY neighborhoods.json /neighborhoods.json
CMD mongoimport --host mongo --db reactivemap --collection neighborhood --type json --file /neighborhoods.json && mongoimport --host mongo --db reactivemap --collection restaurant --type json --file /restaurants.json