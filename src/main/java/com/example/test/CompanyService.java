package com.example.test;

import com.example.test.entity.Branch;
import com.example.test.entity.Company;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CompanyService {

    private static final String GET_BRANCH  = "select * from branch inner join address on branch.address_id = address.id where branch.company_id = ?";
    private static final String str =
            "select * from branch ";
    private DataSource ds;

    private DataSource getDs() {
        if (this.ds == null) {
            Context ctx = null;
            try {
                ctx = new InitialContext();
                this.ds = (DataSource) ctx.lookup("java:/MySqlDS");
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        }
        return this.ds;
    }

    public List<Company> getAll() throws NamingException {
        List<Company> companyList = new ArrayList<>();
        String address;
        try (Connection con = getDs().getConnection();
            PreparedStatement ps = con.prepareStatement("select * from company inner join address on company.address_id = address.id");
        ) {
            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            while(resultSet.next()) {
                Company com = new Company();
                com.setId(resultSet.getLong(1));
                com.setName(resultSet.getString(2));
                com.setType(resultSet.getString(3));
                com.setAddress(resultSet.getString(4));
                address = String.format("г. %s, ул. %s, %s", resultSet.getString(7),resultSet.getString(8), resultSet.getString(9));
                com.setAddress(address);
                companyList.add(com);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return companyList;
    }

    public List<Branch> getBranch(Long id) {
        List<Branch> branches = new ArrayList<>();
        String address;
        try (Connection con = getDs().getConnection();
             PreparedStatement ps =
                     con.prepareStatement(GET_BRANCH);
        ) {
            ps.setLong(1, id);
            ps.execute();
            ResultSet resultSet = ps.getResultSet();

            while(resultSet.next()) {
                Branch branch = new Branch();
                branch.setId(resultSet.getLong(1));
                branch.setName(resultSet.getString(2));
                address = String.format("г. %s, ул. %s, %s", resultSet.getString(6),resultSet.getString(7), resultSet.getString(8));
                branch.setAddress(address);
                branches.add(branch);
                Logger log = Logger.getLogger("test");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return branches;
    }
}
