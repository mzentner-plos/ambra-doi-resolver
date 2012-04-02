/*
 * $HeadURL$
 * $Id$
 * Copyright (c) 2006-2011 by Public Library of Science
 * http://plos.org
 * http://ambraproject.org
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ambraproject.doi;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import sun.reflect.annotation.AnnotationType;

import javax.sql.DataSource;
import java.util.Random;

/**
 * Base class for DOI resolver tests. Creates an empty tables with two tables: Article and Annotations.  Access to the
 * database is available via the {@link #dataSource} property, and rows can be inserted via the {@link
 * #insertArticleRow(int, String)} and {@link #insertAnnotationRow(int, String, int, String)} methods
 *
 * @author alex 9/7/11
 */
public class BaseResolverTest {

  protected DataSource dataSource;

  private JdbcTemplate jdbcTemplate;
  private Random random = new Random();

  @BeforeClass
  public void createDB() {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
    dataSource.setUrl("jdbc:hsqldb:mem:testdb");
    dataSource.setUsername("sa");
    dataSource.setPassword("");
    this.dataSource = dataSource;
    jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.execute(
        "drop table if exists annotation;" +
            "drop table if exists article;" +
            "create table article (" +
            "  articleID bigint not null," +
            "  doi varchar(255) not null," +
            "  primary key (articleID)" +
            ");" +
            "create table annotation (" +
            "  annotationID bigint not null," +
            "  annotationURI varchar(255) not null," +
            "  articleID bigint null," +
            "  type varchar(16) default null," +
            "  primary key (annotationID)" +
            ");" +
            "alter table annotation add foreign key (articleID) references article (articleID);");
  }

  /**
   * Helper method to insert a row into the embedded Article table
   *
   * @param id  the id of the article
   * @param doi the doi column to insert
   */
  protected void insertArticleRow(int id, String doi) {
    jdbcTemplate.execute("insert into article values (" + id + ",'" + doi + "');");
  }

  /**
   * Helper method to insert a row into the embedded Annotation table
   *
   * @param annotationId  id for the annotation row
   * @param annotationUri doi for the annotation row
   * @param articleID     the id of the article to reference
   * @param type          the string type of the annotation
   */
  protected void insertAnnotationRow(int annotationId, String annotationUri, int articleID, String type) {
    jdbcTemplate.execute(
        "insert into annotation values ("
            + annotationId + ", " +
            "'" + annotationUri + "', "
            + articleID + ", "
            + "'" + type + "')");
  }

}
