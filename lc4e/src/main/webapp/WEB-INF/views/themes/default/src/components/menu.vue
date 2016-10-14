<template xmlns:v-bind="http://www.w3.org/1999/xhtml">
    <template v-for="menu in menus">
        <div class="ui dropdown link item" v-if="menu.children.length > 0">
            <i v-if="menu.icon" v-bind:class="[menu.icon,'icon']"></i>
            <span class="text">{{menu.name}}</span><i class="dropdown icon"></i>

            <div class="menu">
                <menu-tree :menus="menu.children"></menu-tree>
            </div>
        </div>
        <a class="item linked" title="{{menu.name}}" v-link="menu.abbr" v-else>
            <i v-bind:class="[menu.icon,'icon']" v-if="menu.icon"></i>
            {{menu.name}}
        </a>
    </template>
</template>

<script>
    module.exports = {
        name: 'menu-tree',
        props: {
            menus: {
                type: Array
            }
        }
    }
</script>