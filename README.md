#### Royal Institute of Technology KTH - Stockholm
# Simple Search Engine

A simple search engine to index a corpus of documents and search for words with specific query paramteres.
This project is part of the course ID1020 Algorithms and Data Structures.


_This repository contains code written during the fall semester 2016 by Simone Stefani_

### Structure
![alt text](https://github.com/SimoneStefani/simple-search-engine/blob/master/src/main/resources/proj2_schema.png)

### Description
- **Index**: a HashMap that contains all the indexed words as word-list_of_postings key-value pairs.
- **ResultDocument**: an object that links a word (or a set of word) with a document that contains it. It refers to a specific `document` and carries properties related to the words such as `hits`, `populairty` and `relevance` (as tf-idf).

The search engine contains other two HashMaps:
- **DocumentsLength**: keeps track of the length of each processed document.
- **Cache**: contains cached queries


_The the postings (resultDocuments) for each word are sorted dynamically at insertion. Consequently they can be retrieved through binary search._


When the user input query string is processed a parsedQuery is returned in the form of nested sub-query objects. Consequently when searching for a complex query, the parsedQuery can be analysed recursively and the fundamental queries can be then combined with operators.
