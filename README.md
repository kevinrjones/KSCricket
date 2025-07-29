# KSCricket

This is all the code for the 'acs cricket' application, it consists of several projects

## Identity Server

The [application](https://duende.com) I use to manage authentication and authorization

## Admin UI

From [Rock Solid Knowledge](https://identityserver.com). This lets us manage the identity database and is not needed to run the actual application

## ACSStats

This is the web front end to the application. It consists of a C# backend application and an Angular front end. The frontend is where nearly all the work is done, although it's the backend that manages the connection to Identity Server

## acs-server

This is the 'REST' backend where all the work is done, written in [Kotlin](https://kotlinlang.org/) and uses [JOOQ](https://www.jooq.org/) to manage the database access.