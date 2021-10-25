package app.datamining;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.planning.State;
import app.representations.*;

public class DBReader {

    protected Set<Variable> variables;
    
    public DBReader(Set<Variable> variables) {
        this.variables = variables;
    }
    
    /**
     * Reads a database, that is, a list of instantiations, from a CSV
     * file.
     * <p>
     * The expected format is the ';'-separated list of variable names
     * as the first line, then one ';'-separated list of values per instance
     * each on its own line
     */
    public Database importDB (String filename) {
        try (BufferedReader reader = new BufferedReader (new FileReader (filename))) {
            Database res = this.readDB(reader);
            reader.close();
            return res;
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
    
    public Database readDB(BufferedReader in) throws IOException {
        // Reading variables
        List<Variable> orderedVariables = new ArrayList<>();
        String variableLine = in.readLine();
        for (String variableName: variableLine.split(";")) {
            boolean found = false;
            for (Variable variable: this.variables) {
                if (variable.getName().equals(variableName)) {
                    orderedVariables.add(variable);
                    found = true;
                    break;
                }
            }
            if ( ! found ) {
                throw new IOException("Unknown variable name: " + variableName);
            }
        }
        // Reading instances
        Set<State> instances = new HashSet<State>();
        String line;
        int lineNb = 1;
        while ( (line = in.readLine()) != null ) {
            String [] parts = line.split(";");
            if (parts.length != orderedVariables.size()) {
                throw new IOException("Wrong number of fields on line " + lineNb);
            }
            State instance = new State();
            for (int i = 0; i < parts.length; i++) {
                instance.addAffectedVariable(orderedVariables.get(i), parts[i]);
            }
            instances.add(instance);
            lineNb++;
        }
        return new Database(orderedVariables, instances);
    }

}
