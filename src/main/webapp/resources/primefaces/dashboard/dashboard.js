PrimeFaces.widget.Dashboard=PrimeFaces.widget.BaseWidget.extend({init:function(b){this._super(b);this.cfg.connectWith=this.jqId+" .ui-dashboard-column";this.cfg.placeholder="ui-state-hover";this.cfg.forcePlaceholderSize=true;this.cfg.revert=false;this.cfg.handle=".ui-panel-titlebar";var a=this;if(this.cfg.behaviors){var c=this.cfg.behaviors.reorder;if(c){this.cfg.update=function(h,g){if(this===g.item.parent()[0]){var f=g.item.parent().children().filter(":not(script):visible").index(g.item),i=g.item.parent().parent().children().index(g.item.parent());var d={params:[{name:a.id+"_reordered",value:true},{name:a.id+"_widgetId",value:g.item.attr("id")},{name:a.id+"_itemIndex",value:f},{name:a.id+"_receiverColumnIndex",value:i}]};if(g.sender){d.params.push({name:a.id+"_senderColumnIndex",value:g.sender.parent().children().index(g.sender)})}c.call(a,d)}}}}$(this.jqId+" .ui-dashboard-column").sortable(this.cfg)}});