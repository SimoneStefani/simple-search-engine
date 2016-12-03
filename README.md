#### Royal Institute of Technology KTH - Stockholm
# Simple Search Engine

A simple search engine to index a corpus of documents and search for words with specific query paramteres.
This project is part of the course ID1020 Algorithms and Data Structures.


_This repository contains code written during the fall semester 2016 by Simone Stefani_

### Structure
![alt text](https://github.com/SimoneStefani/simple-search-engine/blob/master/src/main/resources/proj1_schema.png)

### Description
- **Index**: a list that contains all the indexed words wrapped in the `Token` object.
- **Token**: an object that represents a word. It contains a `postingList` which refers to the set of documents that contain the represented word.
- **Posting**: an object that links a word with a document that contains it. It refers to a specific `document` and carries property of the word in the document like `occurrence`, `popularity` and `hits`.

_The tokens in the index and the postings in the postingList are sorted dynamically at insertion. Consequently they can be retrieved through binary search._
