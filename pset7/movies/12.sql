select
m1.title
from
(select movies.title, p1.name from movies
inner join stars on stars.movie_id = movies.id
inner join people as p1 on p1.id = stars.person_id and p1.name = "Johnny Depp") m1
inner join
(select movies.title, p2.name from movies
inner join stars on stars.movie_id = movies.id
inner join people as p2 on p2.id = stars.person_id and p2.name = "Helena Bonham Carter") m2
on m1.title = m2.title

-- credit to https://www.essentialsql.com/get-ready-to-learn-sql-server-22-using-subqueries-in-the-from-clause/ on their syntax for how to have a query act on subqueries
