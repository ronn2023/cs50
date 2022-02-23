SELECT rating, title FROM ratings JOIN movies ON ratings.movie_id = movies.id WHERE year = 2010 ORDER BY rating, title DESC;
--SELECT rating, movie_id FROM ratings WHERE movie_id IN (SELECT id FROM movies WHERE year = 2010);
