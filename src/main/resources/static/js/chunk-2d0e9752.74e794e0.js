(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d0e9752"],{"8e2a":function(e,s,t){"use strict";t.r(s);var a=function(){var e=this,s=e.$createElement,t=e._self._c||s;return t("div",{staticClass:"message"},[t("v-container",{attrs:{fluid:"","grid-system-md":""}},[t("vue-good-table",{attrs:{columns:e.columns,rows:e.messages,theme:"black-rhino"},on:{"on-row-click":e.onRowClick}},[t("div",{attrs:{slot:"emptystate"},slot:"emptystate"},[e._v(e._s(e.$t("dataInfo.EMPTY_DATA")))])])],1)],1)},o=[],l={data(){return{messages:[],columns:[{label:this.$t("message.columns.id"),field:"id",type:"number"},{label:this.$t("message.columns.type"),field:"type"},{label:this.$t("message.columns.createDate"),field:"createDate",type:"date",dateInputFormat:"yyyy-MM-dd HH:mm:ss",dateOutputFormat:"MM-dd HH:mm:ss"},{label:this.$t("message.columns.message"),field:"message"}]}},created(){this.axios.get("/api/messages").then(e=>{"SUCCESS"===e.data.header&&(this.messages=e.data.body)})},methods:{onRowClick(e){e.row&&e.row.message&&this.$store.dispatch("openModal",{title:"message",message:"["+e.row.createDate+"]"+e.row.message})}}},n=l,m=t("2877"),i=t("6544"),d=t.n(i),r=t("a523"),c=Object(m["a"])(n,a,o,!1,null,null,null);s["default"]=c.exports;d()(c,{VContainer:r["a"]})}}]);
//# sourceMappingURL=chunk-2d0e9752.74e794e0.js.map