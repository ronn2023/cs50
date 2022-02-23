select
name
from
(select distinct person_id, name from people inner join stars on people.id = stars.person_id inner join movies on stars.movie_id = movies.id where year = 2004);
