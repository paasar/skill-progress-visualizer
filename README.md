# skill-tree-visualizer

A skill tree visualizer that turns a simple input file into HTML

## Usage

    lein run

## Input explained

The program reads `input.data` which is formatted as follows:

```
Character name
Character class

Section #1 name
  First skill
    Following skill
    Sibling skill to Following skill
  Sibling skill to First skill

Section #2 name
  ...

...
```
Where a skill row contains the following:

    Symbol or Shorthand;Skill name;Activation date or something (if has value skill is shown as active)

## License

Copyright © Ari Paasonen 2017

If you find this useful let me know at paasar at gmail dot com.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
