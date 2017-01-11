PrimeFaces.widget.DataTable=PrimeFaces.widget.BaseWidget.extend({SORT_ORDER:{ASCENDING:1,DESCENDING:-1,UNSORTED:0},init:function(a){this._super(a);this.thead=$(this.jqId+"_head");this.tbody=$(this.jqId+"_data");this.bindEvents()},bindEvents:function(){if(this.cfg.paginator){this.bindPaginator()}this.bindSortEvents();if(this.cfg.selectionMode){this.bindSelection()}this.bindMobileEvents()},bindPaginator:function(){var a=this;this.cfg.paginator.paginate=function(b){a.paginate(b)};this.paginator=new PrimeFaces.widget.Paginator(this.cfg.paginator)},bindSortEvents:function(){var e=this;this.sortableColumns=this.thead.find("> tr > th.ui-sortable-column");for(var b=0;b<this.sortableColumns.length;b++){var d=this.sortableColumns.eq(b),f=d.children("span.ui-sortable-column-icon"),c=null;if(f.hasClass("ui-column-sorted")){if(f.hasClass("ui-icon-arrow-u")){c=this.SORT_ORDER.ASCENDING}else{c=this.SORT_ORDER.DESCENDING}}else{c=this.SORT_ORDER.UNSORTED}d.data("sortorder",c)}this.sortableColumns.on("click.dataTable",function(j){if(!e.shouldSort(j,this)){return}PrimeFaces.clearSelection();var i=$(this),g=i.data("sortorder"),h=(g===e.SORT_ORDER.UNSORTED)?e.SORT_ORDER.ASCENDING:-1*g;e.sort(i,h)});var a=$(this.jqId+"_reflowDD");if(a&&this.cfg.reflow){a.selectmenu();a.change(function(i){var j=$(this).val().split("_");if(j.length>1){var h=e.sortableColumns.eq(parseInt(j[0])),g=parseInt(j[1]);h.data("sortorder",g);h.trigger("click.dataTable")}})}},bindMobileEvents:function(){if(this.cfg.behaviors){var b=this,a="> tr:not(.ui-datatable-empty-message)";$.each(this.cfg.behaviors,function(c,d){b.tbody.off(c,a).on(c,a,null,function(){var f=b.getRowMeta($(this));var e={params:[{name:b.id+"_rowkey",value:f.key}]};d.call(b,e)})})}},shouldSort:function(a){if(this.isEmpty()){return false}return $(a.target).is("th,span")},paginate:function(d){var c=this,b={source:this.id,update:this.id,process:this.id,formId:this.cfg.formId,params:[{name:this.id+"_pagination",value:true},{name:this.id+"_first",value:d.first},{name:this.id+"_rows",value:d.rows},{name:this.id+"_encodeFeature",value:true}],onsuccess:function(g,e,f){PrimeFaces.ajax.Response.handle(g,e,f,{widget:c,handle:function(h){this.updateData(h)}});return true},oncomplete:function(){c.paginator.cfg.page=d.page;c.paginator.updateUI()}};if(this.hasBehavior("page")){var a=this.cfg.behaviors.page;a.call(this,b)}else{PrimeFaces.ajax.Request.handle(b)}},sort:function(d,a){var e=this,b={source:this.id,update:this.id,process:this.id,params:[{name:this.id+"_sorting",value:true},{name:this.id+"_skipChildren",value:true},{name:this.id+"_encodeFeature",value:true},{name:this.id+"_sortKey",value:d.attr("id")},{name:this.id+"_sortDir",value:a}],onsuccess:function(h,f,g){PrimeFaces.ajax.Response.handle(h,f,g,{widget:e,handle:function(i){this.updateData(i);if(this.paginator){this.paginator.setPage(0,true)}this.sortableColumns.filter(".ui-column-sorted").data("sortorder",this.SORT_ORDER.UNSORTED).removeClass("ui-column-sorted").find(".ui-sortable-column-icon").removeClass("ui-icon-arrow-d ui-icon-arrow-u").addClass("ui-icon-bars");d.data("sortorder",a).addClass("ui-column-sorted");var j=d.find(".ui-sortable-column-icon");if(a===this.SORT_ORDER.DESCENDING){j.removeClass("ui-icon-bars ui-icon-arrow-u").addClass("ui-icon-arrow-d")}else{if(a===this.SORT_ORDER.ASCENDING){j.removeClass("ui-icon-bars ui-icon-arrow-d").addClass("ui-icon-arrow-u")}}}});return true},oncomplete:function(h,f,g){if(e.paginator&&g&&e.paginator.cfg.rowCount!==g.totalRecords){e.paginator.setTotalRecords(g.totalRecords)}}};b.params.push({name:this.id+"_sortKey",value:d.attr("id")});b.params.push();if(this.hasBehavior("sort")){var c=this.cfg.behaviors.sort;c.call(this,b)}else{PrimeFaces.ajax.Request.handle(b)}},bindSelection:function(){var b=this;this.selectionHolder=$(this.jqId+"_selection");this.rowSelector="> tr.ui-datatable-selectable";var a=this.selectionHolder.val();this.selection=(a==="")?[]:a.split(",");this.tbody.off("click.dataTable",this.rowSelector).on("click.dataTable",this.rowSelector,null,function(c){b.onRowClick(c,this)})},onRowClick:function(c,b){if($(c.target).is("td,span:not(.ui-c)")){var d=$(b),a=d.hasClass("ui-bar-b");if(a){this.unselectRow(d)}else{if(this.cfg.selectionMode==="single"){this.unselectAllRows()}this.selectRow(d)}}},selectRow:function(b,a){var d=this.findRow(b),c=this.getRowMeta(d);d.addClass("ui-bar-b");this.addSelection(c.key);this.writeSelections();if(!a){this.fireRowSelectEvent(c.key,"rowSelect")}},unselectRow:function(b,a){var d=this.findRow(b),c=this.getRowMeta(d);d.removeClass("ui-bar-b");this.removeSelection(c.key);this.writeSelections();if(!a){this.fireRowUnselectEvent(c.key,"rowUnselect")}},unselectAllRows:function(){this.tbody.children("tr.ui-bar-b").removeClass("ui-bar-b");this.selection=[];this.writeSelections()},fireRowSelectEvent:function(d,a){if(this.cfg.behaviors){var c=this.cfg.behaviors[a];if(c){var b={params:[{name:this.id+"_instantSelectedRowKey",value:d}]};c.call(this,b)}}},fireRowUnselectEvent:function(d,b){if(this.cfg.behaviors){var a=this.cfg.behaviors[b];if(a){var c={params:[{name:this.id+"_instantUnselectedRowKey",value:d}]};a.call(this,c)}}},writeSelections:function(){this.selectionHolder.val(this.selection.join(","))},findRow:function(a){var b=a;if(PrimeFaces.isNumber(a)){b=this.tbody.children("tr:eq("+a+")")}return b},removeSelection:function(a){this.selection=$.grep(this.selection,function(b){return b!=a})},addSelection:function(a){if(!this.isSelected(a)){this.selection.push(a)}},getRowMeta:function(b){var a={index:b.data("ri"),key:b.attr("data-rk")};return a},isSelected:function(a){return PrimeFaces.inArray(this.selection,a)},isEmpty:function(){return this.tbody.children("tr.ui-datatable-empty-message").length===1},updateData:function(a){this.tbody.html(a);this.postUpdateData()},postUpdateData:function(){if(this.cfg.draggableRows){this.makeRowsDraggable()}},hasBehavior:function(a){if(this.cfg.behaviors){return this.cfg.behaviors[a]!=undefined}return false}});