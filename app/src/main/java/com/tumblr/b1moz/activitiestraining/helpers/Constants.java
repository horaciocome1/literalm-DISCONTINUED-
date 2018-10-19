package com.tumblr.b1moz.activitiestraining.helpers;

public class Constants {
    
    private Constants(){};
    
    public static class RealtimeDatabase {
        
        public final static String POEMS_DETAILS_NODE = "poems_details";
        public final static String POEMS_NODE = "poems";
        
    }
    
    public static class Database {
        
        public final static String name = "literalm";
        public final static int version = 6;
        
        public static class TablePoemas {
    
            public static final String name = "poemas";
            
            public static final String col0 = "_id";
            public static final String col1 = "titulo";
            public static final String col2 = "nome_autor";
            public static final String col3 = "conteudo";
            public static final String col4 = "categoria";
            public static final String col5 = "data";
    
            public static final String createTable = "create table " + name + "("
                    + col0 + " text primary key, "
                    + col1 + " text, "
                    + col2 + " text, "
                    + col3 + " text, "
                    + col4 + " text, "
                    + col5 + " text)";
    
            public final static String dropTable = "drop table if exists " + name;
    
            public final static String whereIdClause = col0 + " = ?";
            
        }
        
    }
    
}
