package org.ambraproject.doi;

/**
 * Immutable wrapper for holding info about an annotation - the annotation id, the doi of the annotated article, and the type.
 * @author  Alex Kudlick
 * 4/2/12
 */
public class AnnotationInfo {

  private final Long annotationId;
  private final String articleDoi;
  private final String annotationType;

  public AnnotationInfo(Long annotationId, String articleDoi, String annotationType) {
    this.annotationType = annotationType;
    this.articleDoi = articleDoi;
    this.annotationId = annotationId;
  }

  public Long getAnnotationId() {
    return annotationId;
  }

  public String getArticleDoi() {
    return articleDoi;
  }

  public String getAnnotationType() {
    return annotationType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AnnotationInfo that = (AnnotationInfo) o;

    if (annotationId != null ? !annotationId.equals(that.annotationId) : that.annotationId != null) return false;
    if (annotationType != null ? !annotationType.equals(that.annotationType) : that.annotationType != null)
      return false;
    if (articleDoi != null ? !articleDoi.equals(that.articleDoi) : that.articleDoi != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = annotationId != null ? annotationId.hashCode() : 0;
    result = 31 * result + (articleDoi != null ? articleDoi.hashCode() : 0);
    result = 31 * result + (annotationType != null ? annotationType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "AnnotationInfo{" +
        "annotationId=" + annotationId +
        ", articleDoi='" + articleDoi + '\'' +
        ", annotationType='" + annotationType + '\'' +
        '}';
  }
}
