package com.example.demo;

import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
public class PostTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;

    private Long userId;

    @BeforeEach
    public void init() {
        User user = new User();
        user.setName("name");
        final User save = userRepository.save(user);
        userRepository.flush();
        userId = save.getId();
    }


    @Test
    void updateTest() {

        User savedUser = executeInNewTransaction(() -> {
            User user = userRepository.findById(userId).orElseThrow();
            Post post = new Post();
            post.setId(1L);
            post.setText("dasdasdas");
            post.setUser(user);
            user.getPosts().add(post);
            return userRepository.save(user);
        });

        executeInNewTransaction(() -> {
            User byId = userRepository.findById(userId).orElseThrow();
            final Post newPost = new Post();
            newPost.setId(2L);
            newPost.setText("dasdasdasdas");
            newPost.setUser(byId);
            byId.getPosts().add(newPost);
            return userRepository.save(byId);
        });

    }

    private <T> T executeInNewTransaction(Supplier<T> callable) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate.execute((transactionStatus -> callable.get()));
    }

    @Test
    void saveTest() {
        Post post = new Post();
        post.setText("Some Text");
        postRepository.save(post);
    }

/*
    @Test
    void updateTestBiDirectional() {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = new Post();
        post.setText("Some Text");
        post.setUser(user);
        user.getPosts().add(post);
        userRepository.save(user);
    }
*/

}

