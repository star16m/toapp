(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-7170e9b8"],{"099a":function(e,t){function a(e,t,a){var s=a-1,i=e.length;while(++s<i)if(e[s]===t)return s;return-1}e.exports=a},1681:function(e,t,a){},"266a":function(e,t,a){var s=a("7948");function i(e,t){return s(t,(function(t){return e[t]}))}e.exports=i},"2b03":function(e,t){function a(e,t,a,s){var i=e.length,r=a+(s?1:-1);while(s?r--:++r<i)if(t(e[r],r,e))return r;return-1}e.exports=a},"3ff1":function(e,t,a){var s=a("266a"),i=a("ec69");function r(e){return null==e?[]:s(e,i(e))}e.exports=r},"47f5":function(e,t,a){var s=a("2b03"),i=a("d9a8"),r=a("099a");function l(e,t,a){return t===t?r(e,t,a):s(e,i,a)}e.exports=l},"4b17":function(e,t,a){var s=a("6428");function i(e){var t=s(e),a=t%1;return t===t?a?t-a:t:0}e.exports=i},"50d8":function(e,t){function a(e,t){var a=-1,s=Array(e);while(++a<e)s[a]=t(a);return s}e.exports=a},6428:function(e,t,a){var s=a("b4b0"),i=1/0,r=17976931348623157e292;function l(e){if(!e)return 0===e?e:0;if(e=s(e),e===i||e===-i){var t=e<0?-1:1;return t*r}return e===e?e:0}e.exports=l},"6fcd":function(e,t,a){var s=a("50d8"),i=a("d370"),r=a("6747"),l=a("0d24"),o=a("c098"),n=a("73ac"),c=Object.prototype,u=c.hasOwnProperty;function d(e,t){var a=r(e),c=!a&&i(e),d=!a&&!c&&l(e),h=!a&&!c&&!d&&n(e),p=a||c||d||h,f=p?s(e.length,String):[],v=f.length;for(var S in e)!t&&!u.call(e,S)||p&&("length"==S||d&&("offset"==S||"parent"==S)||h&&("buffer"==S||"byteLength"==S||"byteOffset"==S)||o(S,v))||f.push(S);return f}e.exports=d},7948:function(e,t){function a(e,t){var a=-1,s=null==e?0:e.length,i=Array(s);while(++a<s)i[a]=t(e[a],a,e);return i}e.exports=a},"8a30":function(e,t,a){var s=a("47f5"),i=a("30c9"),r=a("e2a0"),l=a("4b17"),o=a("3ff1"),n=Math.max;function c(e,t,a,c){e=i(e)?e:o(e),a=a&&!c?l(a):0;var u=e.length;return a<0&&(a=n(u+a,0)),r(e)?a<=u&&e.indexOf(t,a)>-1:!!u&&s(e,t,a)>-1}e.exports=c},"8b37":function(e,t,a){},b4b0:function(e,t,a){var s=a("1a8c"),i=a("ffd6"),r=NaN,l=/^\s+|\s+$/g,o=/^[-+]0x[0-9a-f]+$/i,n=/^0b[01]+$/i,c=/^0o[0-7]+$/i,u=parseInt;function d(e){if("number"==typeof e)return e;if(i(e))return r;if(s(e)){var t="function"==typeof e.valueOf?e.valueOf():e;e=s(t)?t+"":t}if("string"!=typeof e)return 0===e?e:+e;e=e.replace(l,"");var a=n.test(e);return a||c.test(e)?u(e.slice(2),a?2:8):o.test(e)?r:+e}e.exports=d},c098:function(e,t){var a=9007199254740991,s=/^(?:0|[1-9]\d*)$/;function i(e,t){var i=typeof e;return t=null==t?a:t,!!t&&("number"==i||"symbol"!=i&&s.test(e))&&e>-1&&e%1==0&&e<t}e.exports=i},c47f:function(e,t,a){"use strict";a.r(t);var s=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"site"},[e.hasSiteId?a("v-container",[a("h1",[e._v(e._s("["+e.site.id+"] - ["+e.site.name+"] 상세편집"))]),a("v-row",[a("v-col",[a("v-textarea",{attrs:{readonly:"",outlined:"",name:"siteSource",label:"Site source",rows:"8","background-color":"grey lighten-2"},model:{value:e.siteSource,callback:function(t){e.siteSource=t},expression:"siteSource"}})],1),e.accessDetail?a("v-col",[a("v-textarea",{attrs:{readonly:"",outlined:"",name:"siteDetailSource",label:"Site Detail Source",rows:"8","background-color":"grey lighten-2"},model:{value:e.siteDetailSource,callback:function(t){e.siteDetailSource=t},expression:"siteDetailSource"}})],1):e._e()],1),a("v-row",[a("v-col",[a("v-text-field",{attrs:{counter:20,outlined:"",label:"사이트 명"},model:{value:e.site.name,callback:function(t){e.$set(e.site,"name",t)},expression:"site.name"}})],1),a("v-col",[a("v-text-field",{attrs:{counter:120,outlined:"",label:"사이트 검색 URL"},on:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.changeSelector(t)}},model:{value:e.site.searchUrl,callback:function(t){e.$set(e.site,"searchUrl",t)},expression:"site.searchUrl"}})],1)],1),a("v-row",[a("v-col",[a("v-text-field",{attrs:{label:"pageSelector(각 row 를 포함하는 selector)",placeholder:"상세 페이지 검색 selector",outlined:""},on:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.changeSelector(t)}},model:{value:e.site.pageSelector,callback:function(t){e.$set(e.site,"pageSelector",t)},expression:"site.pageSelector"}})],1)],1),a("v-row",[a("v-col",[a("v-text-field",{attrs:{label:"nameSelector(pageSelector 에서 이름을 추출하는 selector)",placeholder:" nameSelector(pageSelector 에서 이름을 추출하는 selector)",outlined:""},on:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.changeSelector(t)}},model:{value:e.site.nameSelector,callback:function(t){e.$set(e.site,"nameSelector",t)},expression:"site.nameSelector"}})],1),a("v-col",[a("v-text-field",{attrs:{label:"sizeSelector(pageSelector 에서 사이즈를 추출하는 selector)",placeholder:"sizeSelector(pageSelector 에서 사이즈를 추출하는 selector)",outlined:""},on:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.changeSelector(t)}},model:{value:e.site.sizeSelector,callback:function(t){e.$set(e.site,"sizeSelector",t)},expression:"site.sizeSelector"}})],1),a("v-col",[a("v-text-field",{attrs:{label:"dateSelector(pageSelector 에서 날짜를 추출하는 selector)",placeholder:"dateSelector(pageSelector 에서 날짜를 추출하는 selector)",outlined:""},on:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.changeSelector(t)}},model:{value:e.site.dateSelector,callback:function(t){e.$set(e.site,"dateSelector",t)},expression:"site.dateSelector"}})],1)],1),e.accessDetail?a("v-row",[a("v-col",[a("h1",[e._v("상세 페이지용")]),a("ul",[a("li",[e._v("이름이나 size selector 를 지정하지 않는 경우 list 에서 구한 것으로 사용합니다.")])])])],1):e._e(),e.accessDetail?a("v-row",[a("v-col",[a("v-select",{attrs:{items:e.siteExtractResultList,label:"["+e.siteExtractResultList.length+"] 개 사이트 링크",dense:"",outlined:"","item-text":"title","item-value":"linkURL","return-object":""},model:{value:e.selectedSite,callback:function(t){e.selectedSite=t},expression:"selectedSite"}})],1)],1):e._e(),e.accessDetail?a("v-row",[a("v-col",[a("v-text-field",{attrs:{label:"torrentNameSelector(상세 페이지에서 이름을 추출하기 위한 selector)",placeholder:"상세 페이지에서 이름을 추출하기 위한 selector",outlined:""},on:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.changeDetailSelector(t)}},model:{value:e.site.torrentNameSelector,callback:function(t){e.$set(e.site,"torrentNameSelector",t)},expression:"site.torrentNameSelector"}})],1),a("v-col",[a("v-text-field",{attrs:{label:"torrentNameReplace(상세 페이지에서 이름을 추출 한 후, replace 처리를 위한 regex)",placeholder:"상세 페이지에서 이름을 추출 한 후, replace 처리를 위한 regex",outlined:""},on:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.changeDetailSelector(t)}},model:{value:e.site.torrentNameReplace,callback:function(t){e.$set(e.site,"torrentNameReplace",t)},expression:"site.torrentNameReplace"}})],1)],1):e._e(),e.accessDetail?a("v-row",[a("v-col",[a("v-text-field",{attrs:{label:"torrentSizeSelector(상세 페이지에서 파일 사이즈를 추출하기 위한 selector)",placeholder:"상세 페이지에서 파일 사이즈를 추출하기 위한 selector",outlined:""},on:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.changeDetailSelector(t)}},model:{value:e.site.torrentSizeSelector,callback:function(t){e.$set(e.site,"torrentSizeSelector",t)},expression:"site.torrentSizeSelector"}})],1),a("v-col",[a("v-text-field",{attrs:{label:"torrentSizeReplace(상세 페이지에서 파일 사이즈를 추출 한 후, replace 처리를 위한 regex)",placeholder:"상세 페이지에서 파일 사이즈를 추출 한 후, replace 처리를 위한 regex",outlined:""},on:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.changeDetailSelector(t)}},model:{value:e.site.torrentSizeReplace,callback:function(t){e.$set(e.site,"torrentSizeReplace",t)},expression:"site.torrentSizeReplace"}})],1)],1):e._e(),e.accessDetail?a("v-row",[a("v-col",[a("v-text-field",{attrs:{label:"torrentMagnetHashSelector(상세 페이지에서 MagnetHash 를 추출하기 위한 selector)",placeholder:"상세 페이지에서 MagnetHash 를 추출하기 위한 selector",outlined:""},on:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.changeDetailSelector(t)}},model:{value:e.site.torrentMagnetHashSelector,callback:function(t){e.$set(e.site,"torrentMagnetHashSelector",t)},expression:"site.torrentMagnetHashSelector"}})],1),a("v-col",[a("v-text-field",{attrs:{label:"torrentMagnetHashReplace(상세 페이지에서 이름을 추출 한 후, replace 처리를 위한 regex)",placeholder:"상세 페이지에서 이름을 추출 한 후, replace 처리를 위한 regex",outlined:""},on:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.changeDetailSelector(t)}},model:{value:e.site.torrentMagnetHashReplace,callback:function(t){e.$set(e.site,"torrentMagnetHashReplace",t)},expression:"site.torrentMagnetHashReplace"}})],1)],1):e._e(),a("v-row",[a("v-col",[a("v-btn",{staticClass:"btnSave",attrs:{color:"primary",tile:"",outlined:""},on:{click:e.changeSelector}},[a("v-icon",{attrs:{left:""}},[e._v("mdi-check")]),e._v("체크 ")],1),e._v(" "),e.accessDetail?a("v-btn",{staticClass:"btnSave",attrs:{color:"primary",tile:"",outlined:""},on:{click:e.changeDetailSelector}},[a("v-icon",{attrs:{left:""}},[e._v("mdi-check-all")]),e._v("상세 체크 ")],1):e._e(),e._v(" "),a("v-btn",{staticClass:"btnSave",attrs:{color:"primary",tile:"",outlined:""},on:{click:e.save}},[a("v-icon",{attrs:{left:""}},[e._v("mdi-cloud-download-outline")]),e._v("저장 ")],1)],1)],1),a("v-bottom-sheet",{attrs:{persistent:""},model:{value:e.dialog4extractResultList,callback:function(t){e.dialog4extractResultList=t},expression:"dialog4extractResultList"}},[a("v-sheet",{staticClass:"text-center",attrs:{height:"500px"}},[a("v-card",{attrs:{light:""}},[a("v-card-text",[a("div",[e._v(e._s(e.siteExtractResultList.length+"개의 링크를 찾았습니다."+(e.accessDetail?"상세 조회 가능합니다.":"")))]),a("v-btn",{staticClass:"mt-6",attrs:{color:"error"},on:{click:function(t){e.dialog4extractResultList=!e.dialog4extractResultList}}},[e._v("close")])],1),a("v-simple-table",{attrs:{dense:!0,"fixed-header":"",height:"500"},scopedSlots:e._u([{key:"default",fn:function(){return[a("thead",[a("tr",[a("th",{staticClass:"text-left"},[e._v("title")]),a("th",{staticClass:"text-left"},[e._v("linkURL")]),a("th",{staticClass:"text-left"},[e._v("size")]),a("th",{staticClass:"text-left"},[e._v("date")])])]),a("tbody",e._l(e.siteExtractResultList,(function(t,s){return a("tr",{key:s},[a("td",[e._v(e._s(t.title))]),a("td",[e._v(e._s(t.linkURL))]),a("td",[e._v(e._s(t.size))]),a("td",[e._v(e._s(t.date))])])})),0)]},proxy:!0}],null,!1,3499620756)})],1)],1)],1),a("v-bottom-sheet",{attrs:{persistent:""},model:{value:e.dialog4extractDetailResultList,callback:function(t){e.dialog4extractDetailResultList=t},expression:"dialog4extractDetailResultList"}},[a("v-sheet",{staticClass:"text-center"},[a("v-card",{attrs:{light:""}},[a("v-card-actions",[a("v-btn",{staticClass:"mt-6",attrs:{color:"error"},on:{click:function(t){e.dialog4extractDetailResultList=!e.dialog4extractDetailResultList}}},[e._v("close")])],1),a("v-card-title",[a("div",[e._v("상세 페이지 추출 정보")])]),a("v-card-text",[a("v-list-item",[a("v-list-item-subtitle",[e._v("이름: "+e._s(e.detailSiteExtractResult.torrentName))])],1),a("v-list-item",[a("v-list-item-subtitle",[e._v("사이즈: "+e._s(e.detailSiteExtractResult.torrentSize))])],1),a("v-list-item",[a("v-list-item-subtitle",[e._v("magnet: "+e._s(e.detailSiteExtractResult.torrentMagnet))])],1)],1)],1)],1)],1)],1):e._e()],1)},i=[],r=a("13ea"),l=a.n(r),o=a("8a30"),n=a.n(o),c={data(){return{site:{},siteSource:"",siteDetailSource:"",accessDetail:!1,dialog4extractResultList:!1,dialog4extractDetailResultList:!1,siteExtractResultList:[],detailSiteExtractResult:{},selectedSite:{}}},computed:{hasSiteId(){return!l()(this.site)},hasDetails(){return this.detailSiteExtractResult&&(!l()(this.detailSiteExtractResult.torrentName)||!l()(this.detailSiteExtractResult.torrentSize)||!l()(this.detailSiteExtractResult.torrentMagnet))}},async mounted(){let e=this.$route.params.siteId;this.downloadSite(e)},methods:{async downloadSite(e){const t=await this.axios.post(`/api/site/${e}`);"SUCCESS"===t.data.header&&(this.site=t.data.body.site,this.siteSource=t.data.body.result)},async changeSelector(){this.fetchListPage()},async changeDetailSelector(){this.fetchDetailPage()},async fetchListPage(){const e=this.site.id,t=await this.axios.post(`/api/site/${e}/find`,this.site);"SUCCESS"===t.data.header?(this.siteSource=t.data.body.result,t.data.body.extractResultList.length>0?(this.siteExtractResultList=t.data.body.extractResultList,this.accessDetail=this.siteExtractResultList.length>0,this.dialog4extractResultList=!0):this.siteExtractResultList=[]):"NOT_FOUND"===t.data.header?(alert(this.$t("response."+t.data.header,{subject:"다운로드 페이지"})),this.siteSource="",this.siteExtractResultList=[]):(alert(this.$t("response."+t.data.header)),this.siteSource="",this.siteExtractResultList=[])},async fetchDetailPage(){if(l()(this.selectedSite.linkURL))return void alert(this.$t("errors.SELECT_DETAIL_SITE"));const e=this.site.id,t={};t.site=Object.assign({},this.site),t.detailPageUrl=this.selectedSite.linkURL,t.title=this.selectedSite.title,t.size=this.selectedSite.size;const a=await this.axios.post(`/api/site/${e}/findDetail`,t);"SUCCESS"===a.data.header?(this.siteDetailSource=a.data.body.detailPageSource,null===a.data.body.extractDetailResult?this.detailSiteExtractResult={}:this.detailSiteExtractResult=a.data.body.extractDetailResult,this.dialog4extractDetailResultList=this.hasDetails):"NOT_FOUND"===a.data.header?(alert(this.$t("response."+a.data.header,{subject:"상세페이지"})),this.siteDetailSource=""):(alert(this.$t("response."+a.data.header)),this.siteDetailSource="")},async save(){l()(String(this.site.searchUrl))?alert("url is not empty!!"):n()(String(this.site.searchUrl),"[KEYWORD]")?await this.axios.post("/api/site",{id:this.site.id,name:this.site.name,searchUrl:this.site.searchUrl,pageSelector:this.site.pageSelector,nameSelector:this.site.nameSelector,sizeSelector:this.site.sizeSelector,dateSelector:this.site.dateSelector,torrentNameSelector:this.site.torrentNameSelector,torrentNameReplace:this.site.torrentNameReplace,torrentSizeSelector:this.site.torrentSizeSelector,torrentSizeReplace:this.site.torrentSizeReplace,torrentMagnetHashSelector:this.site.torrentMagnetHashSelector,torrentMagnetHashReplace:this.site.torrentMagnetHashReplace}).then(e=>{"SUCCESS"===e.data.header?this.$router.push({name:"site"}):alert(this.$t("response."+e.data.header))}):alert(this.$t("errors.BAD_SEARCH_URL"))}}},u=c,d=a("2877"),h=a("6544"),p=a.n(h),f=(a("d0cd"),a("169a")),v=f["a"].extend({name:"v-bottom-sheet",props:{inset:Boolean,maxWidth:{type:[String,Number],default:"auto"},transition:{type:String,default:"bottom-sheet-transition"}},computed:{classes(){return{...f["a"].options.computed.classes.call(this),"v-bottom-sheet":!0,"v-bottom-sheet--inset":this.inset}}}}),S=a("8336"),x=a("b0af"),g=a("99d9"),m=a("62ad"),b=a("a523"),y=a("132d"),k=a("da13"),R=a("5d23"),_=a("0fd9"),w=a("b974"),D=a("8dd9"),E=(a("8b37"),a("80d2")),C=a("7560"),L=a("58df"),$=Object(L["a"])(C["a"]).extend({name:"v-simple-table",props:{dense:Boolean,fixedHeader:Boolean,height:[Number,String]},computed:{classes(){return{"v-data-table--dense":this.dense,"v-data-table--fixed-height":!!this.height&&!this.fixedHeader,"v-data-table--fixed-header":this.fixedHeader,...this.themeClasses}}},methods:{genWrapper(){return this.$slots.wrapper||this.$createElement("div",{staticClass:"v-data-table__wrapper",style:{height:Object(E["f"])(this.height)}},[this.$createElement("table",this.$slots.default)])}},render(e){return e("div",{staticClass:"v-data-table",class:this.classes},[this.$slots.top,this.genWrapper(),this.$slots.bottom])}}),z=a("8654");a("1681");const H=Object(L["a"])(z["a"]);var N=H.extend({name:"v-textarea",props:{autoGrow:Boolean,noResize:Boolean,rowHeight:{type:[Number,String],default:24,validator:e=>!isNaN(parseFloat(e))},rows:{type:[Number,String],default:5,validator:e=>!isNaN(parseInt(e,10))}},computed:{classes(){return{"v-textarea":!0,"v-textarea--auto-grow":this.autoGrow,"v-textarea--no-resize":this.noResizeHandle,...z["a"].options.computed.classes.call(this)}},noResizeHandle(){return this.noResize||this.autoGrow}},watch:{lazyValue(){this.autoGrow&&this.$nextTick(this.calculateInputHeight)},rowHeight(){this.autoGrow&&this.$nextTick(this.calculateInputHeight)}},mounted(){setTimeout(()=>{this.autoGrow&&this.calculateInputHeight()},0)},methods:{calculateInputHeight(){const e=this.$refs.input;if(!e)return;e.style.height="0";const t=e.scrollHeight,a=parseInt(this.rows,10)*parseFloat(this.rowHeight);e.style.height=Math.max(a,t)+"px"},genInput(){const e=z["a"].options.methods.genInput.call(this);return e.tag="textarea",delete e.data.attrs.type,e.data.attrs.rows=this.rows,e},onInput(e){z["a"].options.methods.onInput.call(this,e),this.autoGrow&&this.calculateInputHeight()},onKeyDown(e){this.isFocused&&13===e.keyCode&&e.stopPropagation(),this.$emit("keydown",e)}}}),O=Object(d["a"])(u,s,i,!1,null,null,null);t["default"]=O.exports;p()(O,{VBottomSheet:v,VBtn:S["a"],VCard:x["a"],VCardActions:g["a"],VCardText:g["c"],VCardTitle:g["d"],VCol:m["a"],VContainer:b["a"],VIcon:y["a"],VListItem:k["a"],VListItemSubtitle:R["b"],VRow:_["a"],VSelect:w["a"],VSheet:D["a"],VSimpleTable:$,VTextField:z["a"],VTextarea:N})},d0cd:function(e,t,a){},d9a8:function(e,t){function a(e){return e!==e}e.exports=a},e2a0:function(e,t,a){var s=a("3729"),i=a("6747"),r=a("1310"),l="[object String]";function o(e){return"string"==typeof e||!i(e)&&r(e)&&s(e)==l}e.exports=o},ec69:function(e,t,a){var s=a("6fcd"),i=a("03dd"),r=a("30c9");function l(e){return r(e)?s(e):i(e)}e.exports=l},ffd6:function(e,t,a){var s=a("3729"),i=a("1310"),r="[object Symbol]";function l(e){return"symbol"==typeof e||i(e)&&s(e)==r}e.exports=l}}]);
//# sourceMappingURL=chunk-7170e9b8.7e74652b.js.map