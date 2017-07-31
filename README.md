# skill-progress-visualizer

A skill progress visualizer that turns a simple input file into nice HTML

![skill_generator_cthulhu](https://user-images.githubusercontent.com/1344167/28764546-a9697c16-75ce-11e7-9e42-2913dffc7688.png)

## Usage

    lein run

## Input explained

The program reads `input.data` which is formatted as follows:

```
Character name
Character class
Character description

Section #1 name
First skill
Following skill
Third skill
...

Section #2 name
...

...
```
Where a skill row contains the following:

    Exp;Skill name;Activation date or something (if has value skill is shown as achieved)

## License

Copyright © Ari Paasonen 2017

If you find this useful let me know at paasar at gmail dot com.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
