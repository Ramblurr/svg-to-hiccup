# svg-to-hiccup

> Convert your svg icons to hiccup.

Passes them through [svgo][svgo] to optimize and minify them and spits out valid hiccup.

## Setup

As per [nbb's docs][nbb-deps] we do some hoop jumping to use clojure deps with nbb.

``` sh
bb npm-install
bb write-deps
```


### Usage

``` sh
npx nbb -m cleanup <path to a svg file>
```


[nbb-deps]: https://github.com/babashka/nbb/blob/main/doc/dependencies.mdb
[svgo]: https://github.com/svg/svgo
