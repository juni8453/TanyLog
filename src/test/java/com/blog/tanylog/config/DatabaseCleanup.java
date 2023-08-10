package com.blog.tanylog.config;

import com.google.common.base.CaseFormat;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DatabaseCleanup {

  @PersistenceContext
  private EntityManager entityManager;

  private List<String> tableNames;

  @PostConstruct
  public void initializeTableNames() {
    tableNames = entityManager.getMetamodel().getEntities().stream()
        .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
        .map(e -> {
          Table tableAnnotation = e.getJavaType().getAnnotation(Table.class);
          if (tableAnnotation != null && !tableAnnotation.name().isEmpty()) {
            return tableAnnotation.name();
          } else {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName());
          }
        })
        .collect(Collectors.toList());
  }

  @Transactional
  public void execute() {
    entityManager.flush();

    // 외래 키 제약 조건 해제
    entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

    for (String tableName : tableNames) {
      // DELETE 문으로 레코드 삭제
      entityManager.createNativeQuery("DELETE FROM " + tableName).executeUpdate();

      // AUTO_INCREMENT 값을 초기화
      entityManager.createNativeQuery(
          "ALTER TABLE " + tableName + " AUTO_INCREMENT = 1").executeUpdate();
    }

    // 외래 키 제약 조건 복원
    entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
  }
}

