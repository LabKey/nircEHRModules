Ext4.define('NIRC_EHR.panel.LocationFilterType', {
    extend: 'EHR.panel.LocationFilterType',
    alias: 'widget.nirc_ehr-locationfiltertype',

    getItems: function(){
        var toAdd = [], searchItems = [];
        var ctx = this.filterContext;

        toAdd.push({
            width: 200,
            html: 'Search By Location:<br>'
                    + '<div style="width: 175px;"><i>(Note: when you select an area, '
                    + 'the corresponding rooms will be selected in the room field.)</i></div>'
        });

        if(this.searchOptions.area) {
            searchItems.push({
                xtype: 'ehr-areafield',
                itemId: 'areaField',
                fieldLabel: 'Area(s)',
                pairedWithRoomField: true,
                getRoomField: function(){
                    return this.up('panel').down('#roomField');
                },
                value: ctx.area ? ctx.area.split(',') :  null
            })
        }

        if(this.searchOptions.room) {
            searchItems.push({
                xtype: 'nirc_ehr-roomfield',
                itemId: 'roomField',
                fieldLabel: 'Room',
                value: ctx.room ? ctx.room.split(',') :  null,
                listeners: {
                    change: function(field){
                        var areaField = field.up('panel').down('#areaField');
                        areaField.reset();
                    }
                }
            })
        }

        if(this.searchOptions.cage) {
            searchItems.push({
                xtype: 'ehr-cagefield',
                itemId: 'cageField',
                fieldLabel: 'Cage',
                name: 'cageField',
                value: ctx.cage
            })
        }

        toAdd.push({
            xtype: 'panel',
            defaults: {
                border: false,
                width: 200,
                labelWidth: 90,
                labelAlign: 'top'
            },
            keys: [{
                key: Ext4.EventObject.ENTER,
                handler: this.tabbedReportPanel.onSubmit,
                scope: this.tabbedReportPanel
            }],
            items: searchItems
        });

        return toAdd;
    },

    getTitle: function(){
        var title = [];
        var area, room, cage;

        var roomField = this.down('#roomField');
        if(roomField) {
            room = roomField.getDisplayValue();
            if (Ext4.isArray(room)) {
                if (room.length < 8)
                    room = 'Room: ' + room.join(', ');
                else
                    room = 'Multiple rooms selected';
            }
        }

        var cageField = this.down('#cageField');
        if(cageField)
            cage = cageField.getValue();

        var areaField = this.down('#areaField');
        if(areaField) {
            area = areaField.getValue();
            area = Ext4.isArray(area) ? area.join(', ') : area;
        }

        //see note in getFilterArray() about area/room
        if (area && !room)
            title.push('Area: ' + area);

        if (room)
            title.push(room);

        if (cage)
            title.push('Cage: ' + cage);

        return title.join(', ');
    }
});