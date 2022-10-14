# svg-to-hiccup

> Convert your svg icons to hiccup.

Passes them through [svgo][svgo] to optimize and minify them and spits out valid hiccup.


## Usage

``` sh
$ git clone https://github.com/Ramblurr/svg-to-hiccup.git
$ cd svg-to-hiccup
$ npm install
$ npx nbb -m svg-to-hiccup test.svg
[:svg {:xmlns http://www.w3.org/2000/svg, ....
```


## License

See [LICENSE](./LICENSE)

* `test.svg` - Sad by [papergarden](https://thenounproject.com/icon/sad-4685735/). [CC 3.0](https://creativecommons.org/licenses/by/3.0/) licensed.


[svgo]: https://github.com/svg/svgo
