(defproject
  boot-project
  "0.0.0-SNAPSHOT"
  :dependencies
  [[com.google.guava/guava "19.0"]
   [com.google.javascript/closure-compiler "v20160315"]
   [com.google.javascript/closure-compiler-externs "v20160315"]
   [org.clojure/data.priority-map "0.0.7"]
   [org.clojure/google-closure-library "0.0-20160609-f42b4a24"]
   [org.clojure/google-closure-library-third-party
    "0.0-20160609-f42b4a24"]
   [org.clojure/tools.reader "1.0.0-beta3"]
   [ring/ring-core "1.6.0-beta5"]
   [adzerk/boot-cljs "1.7.228-1"]
   [com.cemerick/piggieback "0.2.1" :scope "test"]
   [weasel "0.7.0" :scope "test"]
   [org.clojure/tools.nrepl "0.2.12" :scope "test"]
   [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
   [adzerk/boot-reload "0.4.12" :scope "test"]
   [compojure "1.6.0-beta1"]
   [hoplon/boot-hoplon "0.2.2"]
   [hoplon "6.0.0-alpha16"]
   [org.clojure/clojure "1.9.0-alpha10"]
   [org.clojure/clojurescript "1.9.198"]
   [pandeiro/boot-http "0.7.3"]
   [http-kit "2.2.0"]
   [ring/ring-defaults "0.2.1"]
   [org.clojure/core.async "0.2.385"]
   [com.taoensso/sente "1.10.0"]]
  :source-paths
  ["src" "assets"])
