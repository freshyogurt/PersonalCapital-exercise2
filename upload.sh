#!/usr/bin/env bash

for i in {1..89}
do
  curl -XPOST https://search-personalcapital-gfo5mzdnneaonjafu5ml54k5by.us-east-1.es.amazonaws.com/_bulk --data-binary @json/data$1.json -H 'Content-Type: application/json'
done