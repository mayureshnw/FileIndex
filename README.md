#FileIndex

A program that indexes all the files in a given directory.
The initial release allowed file indexing using the MySQL datbases. This turned out to be extremely time consuming and search was slow as hell. Another major problem was that only exact filenames would be found.

As a solution I have moved to Apache Lucene - the Engine that powers SolR.
Lucene goes for grmatical analysis and tokenizing hence providing a better set of results for a query.
It follows inverse indexing so although adding content is a little expensive on time but searching in a huge database is pretty fast.

##To-Do

* ~~Use B tress for indexing.~~
* Make multiple searches faster
