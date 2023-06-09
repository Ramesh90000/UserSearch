package com.user.search.request;

import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SearchRequest {
    private String username;
    private String firstName;
    private String lastName;
}

