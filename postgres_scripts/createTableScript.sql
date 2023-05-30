DROP TABLE IF EXISTS problem_instances;
DROP TABLE IF EXISTS edges;
DROP TABLE IF EXISTS nodes;
DROP TABLE IF EXISTS candidates CASCADE;
DROP TABLE IF EXISTS generations;
DROP TABLE IF EXISTS solutions;




CREATE TABLE problem_instances(
	id SERIAL PRIMARY KEY,
	name TEXT UNIQUE,
	nodes_no int NOT NULL,
	edges_no int NOT NULL
);

CREATE TABLE edges(
	problem_id int,
	edge_no int NOT NULL,
	node1 int NOT NULL,
	node2 int NOT NULL,
	PRIMARY KEY(problem_id, edge_no),
	UNIQUE(problem_id, edge_no),
	CONSTRAINT fk_edges_problem_id
		FOREIGN KEY(problem_id)
			REFERENCES problem_instances(id)
);

CREATE TABLE solutions(
	id SERIAL PRIMARY KEY,
	problem_id int,
	mutation_prob DOUBLE PRECISION NOT NULL,
	crossover_prob DOUBLE PRECISION NOT NULL,
	population_size int NOT NULL,
	generations_no int NOT NULL,
	CONSTRAINT fk_solutions_problem_id
		FOREIGN KEY(problem_id)
			REFERENCES problem_instances(id)
);

ALTER TABLE solutions ADD COLUMN overall_best_candidate TEXT;
ALTER TABLE solutions ADD COLUMN overall_best_score INT;

CREATE TABLE generations(
	id SERIAL PRIMARY KEY,
	solution_id int,
	generation int,
	best_candidate TEXT,
	best_score INT
--	UNIQUE(solution_id, generation)
-- 	CONSTRAINT generations_best_candidate_id
-- 		FOREIGN KEY(best_candidate_id)
-- 			REFERENCES candidates(id)
);
delete from generations;

alter table generations add column final_gen boolean;
alter table generations alter column final_gen set default false



-- alter table generations
-- DROP CONSTRAINT generations_solution_id_generation_key;

INSERT INTO generations (id, solution_id, best_candidate) 
VALUES (57351, 1, 
		'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus. Nullam accumsan lorem in dui. Cras ultricies mi eu turpis hendrerit fringilla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; In ac dui quis mi consectetuer lacinia. Nam pretium turpis et arcu. Duis arcu tortor, suscipit eget, imperdiet nec, imperdiet iaculis, ipsum. Sed aliquam ultrices mauris. Integer ante arcu, accumsan a, consectetuer eget, posuere ut, mauris. Praesent adipiscing. Phasellus ullamcorper ipsum rutrum nunc. Nunc nonummy metus. Vestibulum volutpat pretium libero. Cras id dui. Aenean ut eros et nisl sagittis vestibulum. Nullam nulla eros, ultricies sit amet, nonummy id, imperdiet feugiat, pede. Sed lectus. Donec mollis hendrerit risus. Phasellus nec sem in justo pellentesque facilisis. Etiam imperdiet imperdiet orci. Nunc nec neque. Phasellus leo dolor, tempus non, auctor et, hendrerit quis, nisi. Curabitur ligula sapien, tincidunt non, euismod vitae, posuere imperdiet, leo. Maecenas malesuada. Praesent congue erat at massa. Sed cursus turpis vitae tortor. Donec posuere vulputate arcu. Phasellus accumsan cursus velit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Sed aliquam, nisi quis porttitor congue, elit erat euismod orci, ac placerat dolor lectus quis orci. Phasellus consectetuer vestibulum elit. Aenean tellus metus, bibendum sed, posuere ac, mattis non, nunc. Vestibulum fringilla pede sit amet augue. In turpis. Pellentesque posuere. Praesent turpis. Aenean posuere, tortor sed cursus feugiat, nunc augue blandit nunc, eu sollicitudin urna dolor sagittis lacus. Donec elit libero, sodales nec, volutpat a, suscipit non, turpis. Nullam sagittis. Suspendisse pulvinar, augue ac venenatis condimentum, sem libero volutpat nibh, nec pellentesque velit pede quis nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Fusce id purus. Ut varius tincidunt libero. Phasellus dolor. Maecenas vestibulum mollis'
	   );

CREATE TABLE candidates(
	id SERIAL PRIMARY KEY,
	generation_id int,
	candidate_index int,
--	UNIQUE(generation_id, candidate_index),
	CONSTRAINT fk_candidates_generation_id
		FOREIGN KEY(generation_id)
			REFERENCES generations(id)
);

ALTER TABLE generations ADD CONSTRAINT generations_best_candidate_id
		FOREIGN KEY(best_candidate_id)
			REFERENCES candidates(id);
			
-- ALTER TABLE generations DROP CONSTRAINT generations_best_candidate_id;

ALTER TABLE generations ADD CONSTRAINT generations_best_candidate_id
		FOREIGN KEY(best_candidate_id)
			REFERENCES candidates(id)
			ON DELETE CASCADE;

CREATE TABLE nodes(
	id SERIAL PRIMARY KEY,
	candidate_id int,
	node int,
	color int,
	CONSTRAINT fk_nodes_candidate_id
		FOREIGN KEY(candidate_id)
			REFERENCES candidates(id)
);



