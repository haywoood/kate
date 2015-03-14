# kate

work with directories, apply middleware to files, build the output

```clojure
(-> (Kate {:source "site"})
    markdown
    (templates :handlebars)
    build)
```
