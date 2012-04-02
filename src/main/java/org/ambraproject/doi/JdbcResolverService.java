/* $HeadURL:: $
 * $Id: JdbcResolverService.java 10266 2012-02-09 19:41:41Z akudlick $
 *
 * Copyright (c) 2006-2010 by Public Library of Science
 * http://plos.org
 * http://ambraproject.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ambraproject.doi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Resolver for the rdf:type of a DOI-URI.
 *
 * @author Alex Kudlick
 */
public class JdbcResolverService implements ResolverDAOService {
  private static final Logger log = LoggerFactory.getLogger(JdbcResolverService.class);
  private final JdbcTemplate jdbcTemplate;

  public JdbcResolverService(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public boolean doiIsArticle(String doi) {
    log.debug("looking up doi {} in Article table", doi);
    int count = jdbcTemplate
        .queryForInt("select count(*) from article where doi = ?",
            new Object[]{doi});
    return count != 0;
  }

  @Override
  public AnnotationInfo getAnnotationInfo(String doi) {
    log.debug("looking up doi {} in Annotation table", doi);
    try {
      return (AnnotationInfo) jdbcTemplate.query(
          "select an.annotationID, a.doi, an.type from annotation an " +
              " inner join article a on an.articleID = a.articleID " +
              " where an.annotationURI = ?",
          new Object[]{doi},
          new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
              return new AnnotationInfo(
                  rs.getLong(1), //annotationId
                  rs.getString(2), //article doi
                  rs.getString(3) //type
              );
            }
          }).get(0);
    } catch (IndexOutOfBoundsException e) {
      //no annotation
      return null;
    }
  }

}
