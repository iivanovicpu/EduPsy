package hr.iivanovic.psyedu.controllers;

import org.sql2o.Sql2o;

import hr.iivanovic.psyedu.db.Model;
import hr.iivanovic.psyedu.db.Sql2oModel;
import hr.iivanovic.psyedu.util.DbUtil;

/**
 * @author iivanovic
 * @date 31.08.16.
 */
public class AbstractController {
//    public static Sql2o sql2o = DbUtil.getH2DataSource();
    public static Model dbProvider = Sql2oModel.getInstance();

}
