package com.bgcg.queryexecute.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class QueryController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @GetMapping("/")
    public String queryHome(){
        System.out.println("At home page");
        return "login";
    }


    @PostMapping("/executeQuery")
    public String queryExecutor(@RequestParam("sqlQuery") String sqlQuery, @RequestParam("database") String database, Model model){
//        model.addAttribute("name", "Aarti");
//        model.addAttribute("city", "London");
        System.out.println(sqlQuery);
        System.out.println(database);
        testConnection();
        DataSource dataSource = getDataSource(database);
        JdbcTemplate tempJdbcTemplate = new JdbcTemplate(dataSource);
        try {
            if (sqlQuery.trim().toLowerCase().startsWith("select")){
                List<Map<String, Object>> result = tempJdbcTemplate.queryForList(sqlQuery);

                Set<String> columnNames = null;
                if (!result.isEmpty()) {
                    columnNames = result.get(0).keySet();
                }

                model.addAttribute("columns", columnNames);

                model.addAttribute("result", result);
            } else {
                int updateCount = tempJdbcTemplate.update(sqlQuery);
                model.addAttribute("result", "Query executed successfully. Rows Affected "+ updateCount);
            }
        } catch (Exception e) {
//            throw new RuntimeException(e);
            model.addAttribute("result", "Error executing query: " + e.getMessage());
        }

        return "queryResult";
    }

    public void testConnection() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null) {
                System.out.println("Connection to the database successful!");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    connecting to the DB.
    private DataSource getDataSource(String database){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        if ("mysql".equals(database)){
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://localhost:3306/supportdb");
            dataSource.setUsername("supportdb");
            dataSource.setPassword("supportdb");
        } else if ("sybase".equals(database)){
            dataSource.setDriverClassName("");
            dataSource.setUrl("");
            dataSource.setUsername("");
            dataSource.setPassword("");
        }

        return dataSource;
    }
}
