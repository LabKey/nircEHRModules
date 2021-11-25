Ext4.define('NIRC_EHR.form.field.RoomField', {
    extend: 'Ext.ux.CheckCombo',
    alias: 'widget.nirc_ehr-roomfield',
    fieldLabel: 'Room',
    forceSelection: true,
    showOccupiedOnly: false,
    addAllSelector: true,

    initComponent: function(){
        Ext4.apply(this, {
            expandToFitContent: true,
            queryMode: 'local',
            anyMatch: true,
            store: {
                type: 'labkey-store',
                schemaName: 'ehr_lookups',
                queryName: 'rooms',
                columns: 'room,room_sortValue,name, area',
                sort: 'room_sortValue',
                filterArray: this.getStoreFilterArray(),
                autoLoad: true
            },
            valueField: 'room',
            displayField: 'name'
        });

        if (!Ext4.isDefined(this.initialConfig.multiSelect)){
            this.multiSelect = true;
        }

        this.callParent();

        this.on('render', function(field){
            field.el.set({autocomplete: 'off'});
        });
    },

    getStoreFilterArray: function(){
        var ret = [LABKEY.Filter.create('datedisabled', null, LABKEY.Filter.Types.ISBLANK)];
        if (this.showOccupiedOnly)
            ret.push(LABKEY.Filter.create('utilization/totalAnimals', 0, LABKEY.Filter.Types.GREATER_THAN));

        return ret;
    },

    selectByAreas: function(areas){
        if (!this.rendered || !this.store){
            this.on('afterrender', function(field){
                field.selectByAreas(areas);
            }, this, {single: true});

            return;
        }

        this.store.clearFilter();
        if (!this.store.getCount()){
            this.store.on('load', function(store){
                this.selectByAreas(areas);
            }, this, {single: true});
        }
        else {
            if (areas && areas.length){
                var values = [];
                this.store.each(function(rec){
                    if (areas.indexOf(rec.get('area')) != -1){
                        values.push(rec.get('room'));
                    }
                }, this);

                this.setValue(values);
            }
        }
    },

    filterByAreas: function(areas){
        this.store.clearFilter();
        if (!this.rendered){
            this.on('afterrender', function(field){
                field.filterByAreas(areas);
            }, this, {single: true});
        }
        else if (!this.store.getCount()){
            this.store.on('load', function(store){
                this.filterByAreas(areas);
            }, this, {single: true});
        }
        else {
            if (areas && areas.length){
                this.store.filterBy(function(rec){
                    return areas.indexOf(rec.get('area')) != -1;
                }, this);
            }
        }
    }
});