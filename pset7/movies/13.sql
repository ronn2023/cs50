select
DISTINCT m2.name
from
(select movies.title, p1.name, stars.movie_id from movies
inner join stars on stars.movie_id = movies.id
inner join people as p1 on p1.id = stars.person_id and p1.name = "Kevin Bacon" and p1.birth = 1958) m1
inner join
(select movies.title, p2.name, stars.movie_id from movies
inner join stars on stars.movie_id = movies.id
inner join people as p2 on p2.id = stars.person_id and p2.name != "Kevin Bacon") m2
on m1.movie_id = m2.movie_id

-- credit to https://www.essentialsql.com/get-ready-to-learn-sql-server-22-using-subqueries-in-the-from-clause/ on their syntax for how to have a query act on subqueries
