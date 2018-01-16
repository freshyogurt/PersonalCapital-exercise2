# Personal Capital Exercise 2
A micro service that invokes AWS elastic search and makes it available using API gateway and Lambda.
## Exercise Description
Using Java, write a micro service that invokes AWS elastic search and make it available using API gateway.
Put your code in github and email your results to me.    
1. Test Data - http://askebsa.dol.gov/FOIA%20Files/2016/Latest/F_5500_2016_Latest.zip
2. Search should be allowed by Plan name, Sponsor name and Sponsor State   
3. Use AWS best practices
## Dataset Analysis
1. CSV format
2. 132.5 MB
## Steps
1. Convert dataset to a JSON format accepted by AWS Elasticsearch Service. This is done by **csv2json.java**. Key points of code:
    * Handles large dataset
    * Inserts content required by Elasticsearch.
    * Chunks one dataset to multiple json files to meet upload size limitation.
2. Create ES domain
    * Instance type: t2.small
3. Upload dataset using**upload.sh**. This script uses a loop to upload all json files to ES by curl command.
4. Confirm index count and mapping in AWS console:
    * Services → personalcapital (my domain) → Indices → plans (my index)
5. Create a new Lambda function (Plans). Function code is**ProxyWithStream.java**. Since this function needs to communicate with ES, set timeout to 15 second.
6. Create a new API (PersonalCapital) in AWS API Gateway. Under this API, create a new resource (plans), a new Get method. More details about the method:
    * Integration type: Lambda
    * Use Lambda Proxy integration
    * Lambda Function: Plans
## Test
1. Search by plan name (HOCKENBERRY INSURANCE AGENCY RETIREMENT PLAN)
https://ckvik2lmob.execute-api.us-east-1.amazonaws.com/test/plans?planName=HOCKENBERRY%20INSURANCE%20AGENCY%20RETIREMENT%20PLAN
2. Search by sponsor name (HOCKENBERRY INSURANCE AGENCY)
https://ckvik2lmob.execute-api.us-east-1.amazonaws.com/test/plans?sponsorName=HOCKENBERRY%20INSURANCE%20AGENCY
3. Search by sponsor location us state (CA)
https://ckvik2lmob.execute-api.us-east-1.amazonaws.com/test/plans?sponsorLocState=ca
4. Search by sponsor mail us state (CA)
https://ckvik2lmob.execute-api.us-east-1.amazonaws.com/test/plans?sponsorMailState=ca
