# Introduction

This repository is one of the main components of the RIHA infrastructure. RIHA is being maintained and developed by Estonian Information System Authority.

## Prerequisits of submitting a change in RIHA-Browser

- Your code is reasonably covered with unit tests
- Your code has been tested
- RIHA-Browser's API description (swagger.yaml) has been updated

Updating the API description is automated and should be regenerated before each merge/pull request. API descriptions are generated using a [swagger maven plugin](https://github.com/kongchen/swagger-maven-plugin) which uses annotations in Java code to generate a new desciption. To run the generation, use 
 `mvn swagger:generate` which will generate a swagger.yaml file in `src/main/resources/static` folder.
