CREATE OR REPLACE FUNCTION parse_file(file_path TEXT, instance_name TEXT) RETURNS VOID AS $$
DECLARE
    file_handle TEXT;
    file_line TEXT;
    file_lines TEXT[];
	file_fields TEXT[];
    is_first_line BOOLEAN := true;
	problem_instance_id INT;
BEGIN
    file_handle := pg_read_file(file_path);
	file_line := '';
    file_lines := regexp_split_to_array(file_handle, E'\n');
    
    FOREACH file_line IN ARRAY file_lines
    LOOP
        IF is_first_line THEN
            file_fields := regexp_split_to_array(file_line, E' ');
            RAISE NOTICE 'First line: %', file_line;
            

			DECLARE
                v_nodes_no INTEGER := CAST(file_fields[1] AS INTEGER);
                v_edges_no INTEGER := CAST(file_fields[2] AS INTEGER);
            BEGIN
				RAISE NOTICE 'Read fields: %, %', v_nodes_no, v_edges_no;
				
                INSERT INTO problem_instances (name, nodes_no, edges_no)
                VALUES (instance_name, v_nodes_no, v_edges_no);
				
				SELECT MAX(id) INTO problem_instance_id FROM problem_instances;
            END;
			
            is_first_line := false;  
        ELSE
            RAISE NOTICE 'Read line: %', file_line;
			file_fields := regexp_split_to_array(file_line, E' ');

            DECLARE
				v_ignore TEXT := file_fields[1];
                v_node1 INTEGER := CAST(file_fields[2] AS INTEGER);
                v_node2 INTEGER := CAST(file_fields[3] AS INTEGER);
				v_edge_no INTEGER;
            BEGIN
			    RAISE NOTICE 'Read fields: %, %, %', v_ignore, v_node1, v_node2;
				v_edge_no = next_edge_no(problem_instance_id);
                INSERT INTO edges (problem_id, edge_no,  node1, node2)
                VALUES (problem_instance_id, v_edge_no, v_node1, v_node2);
            END;
        END IF;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

SELECT parse_file('D:\Facultate\An2sem2\GeneticAlgorithmVisualizer\GeneticAlgorithmVisualizer\src\instances\myciel5.col', 'myciel5');

SELECT * FROM edges;
SELECT * FROM problem_instances;

DELETE FROM edges;
DELETE FROM problem_instances;
