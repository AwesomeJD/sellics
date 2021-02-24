#The assignment:
## Problem statement (in gist)
For the assignment, you have to implement a microservice with a 
single REST endpoint. This endpoint should receive a single keyword 
as an input and should return a score for that same exact keyword. 
The score should be in the range [0 → 100] and represent the 
estimated search-volume (how often Amazon customers search 
for that ​exact ​keyword). A score of 0 means that the 
keyword is practically never searched for, 100 means that this 
is one of the hottest keywords in all of ​amazon.com​ right now.


     Example API Request​ ​(note: 87 is not the real score)

     REQUEST ​GET http://localhost:8080/estimate?keyword=iphone+charger 
     RESPONSE
        {
            “Keyword”:”iphone charger”,
            “score”:87 
        }

#Get the solution running...

The application is a spring-boot application, and can be run by running the main class. ApplicationBootStrap.java

    URL: http://localhost:8080/estimate?keyword=Iphone

Response:

    {
        "keyword": "iphone",
        "score": 60
    }

### Exception scenarios.
When the keyword is blank, we get the below response. The spring-boot validation is used for the validation for o fthe input. 
The Custom Exception Handler is added to take of the exceptions and throw pretty looking exceptions. 

    {
        "errors": [
            {
                "code": "1002",
                "message": "[Keyword cannot be blank.]",
                "type": "BAD USER INPUT"
            }
        ]
    }

When the amazon API is down or some network failure, we get the below response from the service.

    {
         "errors": [
            {
                "code": "1001",
                "message": "Error in the amazon api, please check logs.",
                "type": "Business Error"
            }
        ]
    }

### Points to be noted on the Application.
* The time count/metrics of the API is not measured using tools, like Prometheus and micrometer. 
  Neither a simple System time is used, but the basic observation clearly shows that the API takes
  far less than 10 secs. 
* Few tests have been added. Since the tests were not priority so did not spend more time...
* The exception handling is implemented.
* Validation is implemented for the search keyword using spring-boot validation framework.
* The code is modularised and separation of concerns is focused. 
* The logging has been done quit comprehensively and gives a good insight as 
what is happening when the application runs. 
* Lombok is used, so the IDE should have lombok plugin installed. 

# Questions asked. 

## What assumptions did you make?
1. The Amazon completion api is very fast. :)
2. The amazon api returns exactly 10 suggestions for each on key press. 
3. The value attribute of the suggestions from the API is the key for the solution. 
4. There is an attribute called strategyId in the suggestions from the amazon API, whihc seems uselful, 
but have been ignored here.
   
5. We need to fire only as many API calls to amazon API, as many prefixes we can make out of the 
search key word. Just like Amazon actually does when we do onKeyPress on the search bar. 
   

## How does your algorithm work?
Steps:
Suppose the search is made for **Iphone**.
1. The key word is split into multiple prefixes. Example: I, Ip, Iph, Ipho, Iphon, Iphone.
2. The weight each prefix is calculated using the formula. 
    
        weightOfPrefix = ((length of the search keyword + 1) - (length of the prefix))/(length of the search keyword)

3. Each prefix is fired and suggestions from amazon API are accumulated in a list. 
4. The search for the exact match is done using iteration and once found the search volume in 
   percentage is calculated using the below formula. Here the fullMatchWeight is a configurable
   parameter. This is set to 1 for an exact match and 0.9, 0.8 for all prefix match and partial prefix match
   respectively.

        (weightOfPrefix * 100 * fullMatchWeight)

    Ex: For Iphone, for the prefix I, if the keyword is found as an exact match in the 
    suggestions, then the search volume percentage is 100. 
   Since the weight of prefix is =  ((6 + 1) - 1))/6 = 1.0, and the fullMatchWeight we have set as 1. 
   So the final result is 1.0 * 100 * 1 = 100 %

5. If we have reached here that means we have  not found any exact match. For this scenarios, I have added
other pattern match strategies and have assigned configurable weights to them 
   Ex: The PrefixMatchAll strategy checks for the keyword to be present as a prefix in all the 
   suggestions. If found, the same above formula is used. The only difference is the weight of the 
   strategy is different. For this strategy, the weight assigned is 0.8.
   
## Do you think the (​*hint)​ that we gave you earlier is correct and if so - why?
Yes the hint provided, is kind of correct. 
We are more of concerned on the number of api calls required per prefix. 
The order actually does not matter much since, any pattern match has to be applied on the full set of 
suggestions returned from a single api call to amazon completion api. 

But, if we dig more into the algorithm and provide weight to the iteration, We could utilise the order of the suggestions as well. 
For example, if the first suggestion itself is an exact match to the keyword provided, 
we could calculate a higher percentage than the exact match to the 4th suggestion.


## How precise do you think your outcome is and why?
I believe, everything can be improved. 
The algorithm I could come up with has the ability to be considerably precise. 
The storage of the past searches and more analysis on the history could definitely help in 
making more informed choice on the search volume percentage. 
Would love to hear as to how can I come up with a number to quantify this. 





