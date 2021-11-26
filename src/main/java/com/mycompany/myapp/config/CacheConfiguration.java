package com.mycompany.myapp.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.mycompany.myapp.domain.User.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName());
            createCache(cm, com.mycompany.myapp.domain.User.class.getName() + ".authorities");
            createCache(cm, com.mycompany.myapp.domain.Exercise.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Exercise.class.getName() + ".exerciseImages");
            createCache(cm, com.mycompany.myapp.domain.Exercise.class.getName() + ".muscles");
            createCache(cm, com.mycompany.myapp.domain.Exercise.class.getName() + ".workoutExercises");
            createCache(cm, com.mycompany.myapp.domain.BodyMeasurement.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ExerciseCategory.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ExerciseCategory.class.getName() + ".exercises");
            createCache(cm, com.mycompany.myapp.domain.ExerciseImage.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ExerciseImage.class.getName() + ".exercises");
            createCache(cm, com.mycompany.myapp.domain.MeasurementType.class.getName());
            createCache(cm, com.mycompany.myapp.domain.MeasurementType.class.getName() + ".bodyMeasurements");
            createCache(cm, com.mycompany.myapp.domain.MeasurementType.class.getName() + ".weights");
            createCache(cm, com.mycompany.myapp.domain.Muscle.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Muscle.class.getName() + ".exercises");
            createCache(cm, com.mycompany.myapp.domain.ProgressPhoto.class.getName());
            createCache(cm, com.mycompany.myapp.domain.RepType.class.getName());
            createCache(cm, com.mycompany.myapp.domain.RepType.class.getName() + ".exercises");
            createCache(cm, com.mycompany.myapp.domain.Weight.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Workout.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Workout.class.getName() + ".workoutExercises");
            createCache(cm, com.mycompany.myapp.domain.WorkoutExercise.class.getName());
            createCache(cm, com.mycompany.myapp.domain.WorkoutExercise.class.getName() + ".workoutExerciseSets");
            createCache(cm, com.mycompany.myapp.domain.WorkoutExerciseSet.class.getName());
            createCache(cm, com.mycompany.myapp.domain.WorkoutRoutine.class.getName());
            createCache(cm, com.mycompany.myapp.domain.WorkoutRoutine.class.getName() + ".workouts");
            createCache(cm, com.mycompany.myapp.domain.WorkoutRoutine.class.getName() + ".workoutRoutineGroups");
            createCache(cm, com.mycompany.myapp.domain.WorkoutRoutineExercise.class.getName());
            createCache(cm, com.mycompany.myapp.domain.WorkoutRoutineExercise.class.getName() + ".workoutRoutineExerciseSets");
            createCache(cm, com.mycompany.myapp.domain.WorkoutRoutineExercise.class.getName() + ".workoutExercises");
            createCache(cm, com.mycompany.myapp.domain.WorkoutRoutineExercise.class.getName() + ".workoutRoutines");
            createCache(cm, com.mycompany.myapp.domain.WorkoutRoutineExerciseSet.class.getName());
            createCache(cm, com.mycompany.myapp.domain.WorkoutRoutineGroup.class.getName());
            createCache(cm, com.mycompany.myapp.domain.WorkoutRoutineGroup.class.getName() + ".workoutRoutines");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
