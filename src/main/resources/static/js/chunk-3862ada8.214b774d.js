(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-3862ada8"],{"099a":function(t,e){function i(t,e,i){var a=i-1,r=t.length;while(++a<r)if(t[a]===e)return a;return-1}t.exports=i},"0e8f":function(t,e,i){"use strict";i("20f6");var a=i("e8f2");e["a"]=Object(a["a"])("flex")},"266a":function(t,e,i){var a=i("7948");function r(t,e){return a(e,(function(e){return t[e]}))}t.exports=r},"2b03":function(t,e){function i(t,e,i,a){var r=t.length,s=i+(a?1:-1);while(a?s--:++s<r)if(e(t[s],s,t))return s;return-1}t.exports=i},"3ff1":function(t,e,i){var a=i("266a"),r=i("ec69");function s(t){return null==t?[]:a(t,r(t))}t.exports=s},"47f5":function(t,e,i){var a=i("2b03"),r=i("d9a8"),s=i("099a");function n(t,e,i){return e===e?s(t,e,i):a(t,r,i)}t.exports=n},"4b17":function(t,e,i){var a=i("6428");function r(t){var e=a(t),i=e%1;return e===e?i?e-i:e:0}t.exports=r},"50d8":function(t,e){function i(t,e){var i=-1,a=Array(t);while(++i<t)a[i]=e(i);return a}t.exports=i},6428:function(t,e,i){var a=i("b4b0"),r=1/0,s=17976931348623157e292;function n(t){if(!t)return 0===t?t:0;if(t=a(t),t===r||t===-r){var e=t<0?-1:1;return e*s}return t===t?t:0}t.exports=n},"6fcd":function(t,e,i){var a=i("50d8"),r=i("d370"),s=i("6747"),n=i("0d24"),o=i("c098"),l=i("73ac"),u=Object.prototype,c=u.hasOwnProperty;function d(t,e){var i=s(t),u=!i&&r(t),d=!i&&!u&&n(t),f=!i&&!u&&!d&&l(t),h=i||u||d||f,p=h?a(t.length,String):[],v=p.length;for(var m in t)!e&&!c.call(t,m)||h&&("length"==m||d&&("offset"==m||"parent"==m)||f&&("buffer"==m||"byteLength"==m||"byteOffset"==m)||o(m,v))||p.push(m);return p}t.exports=d},7948:function(t,e){function i(t,e){var i=-1,a=null==t?0:t.length,r=Array(a);while(++i<a)r[i]=e(t[i],i,t);return r}t.exports=i},"8a30":function(t,e,i){var a=i("47f5"),r=i("30c9"),s=i("e2a0"),n=i("4b17"),o=i("3ff1"),l=Math.max;function u(t,e,i,u){t=r(t)?t:o(t),i=i&&!u?n(i):0;var c=t.length;return i<0&&(i=l(c+i,0)),s(t)?i<=c&&t.indexOf(e,i)>-1:!!c&&a(t,e,i)>-1}t.exports=u},b4b0:function(t,e,i){var a=i("1a8c"),r=i("ffd6"),s=NaN,n=/^\s+|\s+$/g,o=/^[-+]0x[0-9a-f]+$/i,l=/^0b[01]+$/i,u=/^0o[0-7]+$/i,c=parseInt;function d(t){if("number"==typeof t)return t;if(r(t))return s;if(a(t)){var e="function"==typeof t.valueOf?t.valueOf():t;t=a(e)?e+"":e}if("string"!=typeof t)return 0===t?t:+t;t=t.replace(n,"");var i=l.test(t);return i||u.test(t)?c(t.slice(2),i?2:8):o.test(t)?s:+t}t.exports=d},c098:function(t,e){var i=9007199254740991,a=/^(?:0|[1-9]\d*)$/;function r(t,e){var r=typeof t;return e=null==e?i:e,!!e&&("number"==r||"symbol"!=r&&a.test(t))&&t>-1&&t%1==0&&t<e}t.exports=r},d9a8:function(t,e){function i(t){return t!==t}t.exports=i},e2a0:function(t,e,i){var a=i("3729"),r=i("6747"),s=i("1310"),n="[object String]";function o(t){return"string"==typeof t||!r(t)&&s(t)&&a(t)==n}t.exports=o},e910:function(t,e,i){"use strict";i.r(e);var a=function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"site"},[i("v-container",{attrs:{fluid:"","grid-system-md":""}},[i("vue-good-table",{attrs:{columns:t.columns,rows:t.sites,theme:"black-rhino","select-options":{enabled:!1,disableSelectInfo:!0}},scopedSlots:t._u([{key:"table-row",fn:function(e){return["delete"==e.column.field?i("span",[i("v-btn",{attrs:{color:"error"},on:{click:function(i){return i.stopPropagation(),t.deleteSite(e.row.id)}}},[t._v("삭제")])],1):t._e(),"editDetail"==e.column.field?i("span",[i("v-btn",{attrs:{color:"info"},on:{click:function(i){return i.stopPropagation(),t.editSite(e.row.id)}}},[t._v("편집")])],1):t._e(),"copy"==e.column.field?i("span",[i("v-btn",{attrs:{color:"warn"},on:{click:function(i){return i.stopPropagation(),t.copySite(e.row.id)}}},[t._v("복제")])],1):t._e(),"enable"==e.column.field?i("span",[e.row.useable?i("v-btn",{attrs:{color:"warn"},on:{click:function(i){return i.stopPropagation(),t.useableSite(e.row)}}},[t._v("비활성화")]):i("v-btn",{attrs:{color:"primary"},on:{click:function(i){return i.stopPropagation(),t.useableSite(e.row)}}},[t._v("활성화")])],1):i("span",[t._v(t._s(e.formattedRow[e.column.field]))])]}}])},[i("div",{attrs:{slot:"emptystate"},slot:"emptystate"},[t._v(t._s(t.$t("dataInfo.EMPTY_DATA")))])])],1),i("v-container",[i("v-layout",{attrs:{fluid:""}},[i("v-flex",{attrs:{shrink:""}},[i("ValidationObserver",{ref:"obs",scopedSlots:t._u([{key:"default",fn:function(e){var a=e.handleSubmit;return[i("v-form",{attrs:{id:"siteForm"},on:{submit:function(e){return e.preventDefault(),a(t.save)}}},[i("v-flex",{attrs:{xs12:"",sm6:"",md4:"","text-xs-right":"","mb-2":"","mt-2":"","pr-2":""}},[i("v-dialog",{attrs:{"max-width":"800px","content-class":"dlgNewEditItem"},scopedSlots:t._u([{key:"activator",fn:function(e){var a=e.on;return[i("v-btn",t._g({staticClass:"btnNewItem",attrs:{color:"primary"}},a),[i("v-icon",{staticClass:"mr-2"},[t._v("mdi-plus")]),t._v(" "+t._s(t.$t("common.NEW_ITEM",{item:t.$t("common.SITE")}))+" ")],1)]}}],null,!0),model:{value:t.dialog,callback:function(e){t.dialog=e},expression:"dialog"}},[i("v-card",{attrs:{light:""}},[i("v-card-title",[i("span",{staticClass:"headline"},[t._v("새로운 SITE")])]),i("v-card-text",[i("v-container",{attrs:{"grid-list-md":""}},[i("v-layout",{attrs:{wrap:""}},[i("v-flex",{attrs:{xs12:"",md6:""}},[i("VTextFieldWithValidation",{attrs:{rules:"required|min:4|max:20",id:"name",name:"name",counter:20,"data-vv-as":t.$t("site.NAME"),label:t.$t("site.NAME")},model:{value:t.site.name,callback:function(e){t.$set(t.site,"name",e)},expression:"site.name"}})],1),i("v-flex",{attrs:{xs12:"",md6:""}},[i("VTextFieldWithValidation",{attrs:{rules:"required|min:10|max:120",id:"url",name:"url",counter:120,"data-vv-as":t.$t("site.URL"),label:t.$t("site.URL")},model:{value:t.site.searchUrl,callback:function(e){t.$set(t.site,"searchUrl",e)},expression:"site.searchUrl"}})],1),i("v-flex",{attrs:{xs12:"",md12:""}},[i("VTextFieldWithValidation",{attrs:{rules:"required|min:4|max:255",id:"pageSelector",name:"pageSelector",counter:120,"data-vv-as":t.$t("site.PAGE_SELECTOR"),label:t.$t("site.PAGE_SELECTOR")},model:{value:t.site.pageSelector,callback:function(e){t.$set(t.site,"pageSelector",e)},expression:"site.pageSelector"}})],1)],1)],1)],1),i("v-card-actions",[i("v-spacer"),i("v-btn",{staticClass:"btnCancel",attrs:{color:"error",tile:"",outlined:""},on:{click:t.close}},[i("v-icon",{attrs:{left:""}},[t._v("mdi-cancel")]),t._v(" "+t._s(t.$t("common.CANCEL"))+" ")],1),i("v-btn",{staticClass:"btnSave",attrs:{color:"primary",tile:"",outlined:"",type:"submit",form:"siteForm"}},[i("v-icon",{attrs:{left:""}},[t._v("mdi-pencil")]),t._v(" "+t._s(t.$t("common.SAVE"))+" ")],1)],1)],1)],1)],1)],1)]}}])})],1)],1)],1)],1)},r=[],s=i("13ea"),n=i.n(s),o=i("8a30"),l=i.n(o),u=function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("ValidationProvider",{attrs:{name:t.$attrs.label,rules:t.rules},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.errors,r=e.valid;return[i("v-text-field",t._g(t._b({attrs:{"error-messages":a,success:r,autocomplete:"off"},model:{value:t.innerValue,callback:function(e){t.innerValue=e},expression:"innerValue"}},"v-text-field",t.$attrs,!1),t.$listeners))]}}])})},c=[],d=i("7bb1"),f={components:{ValidationProvider:d["b"]},props:{rules:{type:[Object,String],default:""},value:{type:null}},data(){return{innerValue:""}},watch:{innerValue(t){this.$emit("input",t)},value(t){this.innerValue=t}},created(){this.value&&(this.innerValue=this.value)}},h=f,p=i("2877"),v=i("6544"),m=i.n(v),b=i("8654"),x=Object(p["a"])(h,u,c,!1,null,null,null),g=x.exports;m()(x,{VTextField:b["a"]});var S={watch:{dialog(t){return!!t||this.close()}},data(){return{sites:[],dialog:!1,columns:[{label:this.$t("site.columns.id"),field:"id",type:"number"},{label:this.$t("site.columns.name"),field:"name"},{label:this.$t("site.columns.searchUrl"),field:"searchUrl"},{label:this.$t("site.columns.delete"),field:"delete"},{label:this.$t("site.columns.editDetail"),field:"editDetail"},{label:this.$t("site.columns.copy"),field:"copy"},{label:this.$t("site.columns.enable"),field:"enable"}],site:{},defaultSite:{},siteExtractResultList:[],foundedElementsNum:0}},components:{ValidationObserver:d["a"],VTextFieldWithValidation:g},methods:{async retrieveSite(){const t=await this.axios.get("/api/sites");this.sites=t.data,this.sites.length>0&&this.refreshEnabledCheckbox()},async deleteSite(t){const e=await this.axios.delete(`/api/site/${t}`);"SUCCESS"===e.data.header&&this.retrieveSite()},editSite(t){this.$router.push({name:"SiteInput",params:{siteId:t}})},async useableSite(t){const e=t.id;await this.$axios.patch(`/api/site/${e}/useable`,{id:e,request:!t.useable}),this.retrieveSite()},refreshEnabledCheckbox(){this.sites.forEach(t=>this.$set(t,"vgtSelected",t.useable))},enableSiteStyle(t){return t.useable?"accent":""},close(){this.dialog=!1,setTimeout(()=>{this.site=Object.assign({},this.defaultSite),requestAnimationFrame(()=>{this.$refs.obs.reset()})},300)},async copySite(t){const e=await this.axios.post(`/api/site/${t}/copy`,{id:this.site.id});"SUCCESS"===e.data.header?this.retrieveSite():alert(this.$t("response."+e.data.header))},async save(){n()(String(this.site.searchUrl))?alert("url is not empty!!"):l()(String(this.site.searchUrl),"[KEYWORD]")?await this.axios.post("/api/site",{id:this.site.id,name:this.site.name,searchUrl:this.site.searchUrl,pageSelector:this.site.pageSelector,useable:!1}).then(t=>{if("SUCCESS"===t.data.header){const e=t.data.body.id;this.$router.push({name:"SiteInput",params:{siteId:e}})}else alert(this.$t("response."+t.data.header))}):alert(this.$t("errors.BAD_SEARCH_URL"))}},mounted(){this.site={},this.retrieveSite()}},_=S,$=i("8336"),y=i("b0af"),w=i("99d9"),V=i("a523"),E=i("169a"),C=i("0e8f"),k=i("58df"),O=i("7e2b"),T=i("3206"),A=Object(k["a"])(O["a"],Object(T["b"])("form")).extend({name:"v-form",inheritAttrs:!1,props:{lazyValidation:Boolean,value:Boolean},data:()=>({inputs:[],watchers:[],errorBag:{}}),watch:{errorBag:{handler(t){const e=Object.values(t).includes(!0);this.$emit("input",!e)},deep:!0,immediate:!0}},methods:{watchInput(t){const e=t=>t.$watch("hasError",e=>{this.$set(this.errorBag,t._uid,e)},{immediate:!0}),i={_uid:t._uid,valid:()=>{},shouldValidate:()=>{}};return this.lazyValidation?i.shouldValidate=t.$watch("shouldValidate",a=>{a&&(this.errorBag.hasOwnProperty(t._uid)||(i.valid=e(t)))}):i.valid=e(t),i},validate(){return 0===this.inputs.filter(t=>!t.validate(!0)).length},reset(){this.inputs.forEach(t=>t.reset()),this.resetErrorBag()},resetErrorBag(){this.lazyValidation&&setTimeout(()=>{this.errorBag={}},0)},resetValidation(){this.inputs.forEach(t=>t.resetValidation()),this.resetErrorBag()},register(t){this.inputs.push(t),this.watchers.push(this.watchInput(t))},unregister(t){const e=this.inputs.find(e=>e._uid===t._uid);if(!e)return;const i=this.watchers.find(t=>t._uid===e._uid);i&&(i.valid(),i.shouldValidate()),this.watchers=this.watchers.filter(t=>t._uid!==e._uid),this.inputs=this.inputs.filter(t=>t._uid!==e._uid),this.$delete(this.errorBag,e._uid)}},render(t){return t("form",{staticClass:"v-form",attrs:{novalidate:!0,...this.attrs$},on:{submit:t=>this.$emit("submit",t)}},this.$slots.default)}}),I=i("132d"),U=i("a722"),B=i("2fa4"),P=Object(p["a"])(_,a,r,!1,null,null,null);e["default"]=P.exports;m()(P,{VBtn:$["a"],VCard:y["a"],VCardActions:w["a"],VCardText:w["c"],VCardTitle:w["d"],VContainer:V["a"],VDialog:E["a"],VFlex:C["a"],VForm:A,VIcon:I["a"],VLayout:U["a"],VSpacer:B["a"]})},ec69:function(t,e,i){var a=i("6fcd"),r=i("03dd"),s=i("30c9");function n(t){return s(t)?a(t):r(t)}t.exports=n},ffd6:function(t,e,i){var a=i("3729"),r=i("1310"),s="[object Symbol]";function n(t){return"symbol"==typeof t||r(t)&&a(t)==s}t.exports=n}}]);
//# sourceMappingURL=chunk-3862ada8.214b774d.js.map