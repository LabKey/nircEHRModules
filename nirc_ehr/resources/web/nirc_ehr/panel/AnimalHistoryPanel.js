/*
 * Copyright (c) 2019 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

LABKEY.Utils.requiresScript("nirc_ehr/utils.js", function () {});

Ext4.define('NIRC_EHR.panel.AnimalHistoryPanel', {
    extend: 'EHR.panel.AnimalHistoryPanel',
    alias: 'widget.nirc_ehr-animalhistorydetailspanel',

    initComponent: function(){
        Ext4.apply(this, {
            filterTypes: this.getAllowedFilterTypes()
        });
        this.callParent(arguments);

    },

    handleFiltersNIRC: function (tab, filters) {
        var filterArray = {
            subjects: NIRC_EHR.Utils.splitIds(this.down('#subjArea').getValue()),
            removable: [],
            nonRemovable: []
        };

        var subjectFieldName;
        if(tab.report) {
            subjectFieldName = tab.report.subjectFieldName;
        }
        else if(tab.items[0].report) {
            subjectFieldName = tab.items[0].report.subjectFieldName;
        }
        if (!subjectFieldName) {
            return filterArray;
        }

        if (filters && filters.length) {
            filterArray.subjects = Ext4.unique(filterArray.subjects.concat(filters)).sort();
            if (filters.length == 1)
                filterArray.nonRemovable.push(LABKEY.Filter.create(subjectFieldName, filters[0], LABKEY.Filter.Types.EQUAL));
            else
                filterArray.nonRemovable.push(LABKEY.Filter.create(subjectFieldName, filters.join(';'), LABKEY.Filter.Types.EQUALS_ONE_OF));
        }

        return filterArray;
    },

    getAllowedFilterTypes: function(){
        if (this.filterTypes && this.filterTypes.length > 0 && this.filterTypes[0].xtype === "ehr-hiddensinglesubjectfiltertype") {
            return [{
                xtype: 'ehr-hiddensinglesubjectfiltertype',
                inputValue: LDK.panel.SingleSubjectFilterType.filterName,
                aliasTable: {
                    schemaName: 'study',
                    queryName: 'alias',
                    idColumn: 'Id',
                    aliasColumn: 'alias'
                },
                loadReport: NIRC_EHR.Utils.singleSubjectLoadReport,
                handleFilters: this.handleFiltersNIRC
            }]
        }

        var allowedFilterTypes = [];
        allowedFilterTypes.push({
            xtype: 'ldk-singlesubjectfiltertype',
            inputValue: LDK.panel.SingleSubjectFilterType.filterName,
            label: 'Single Animal',
            nounSingular: 'Animal',
            aliasTable: {
                schemaName: 'study',
                queryName: 'alias',
                idColumn: 'Id',
                aliasColumn: 'alias'
            },
            loadReport: NIRC_EHR.Utils.singleSubjectLoadReport,
            handleFilters: this.handleFiltersNIRC
        },{
            xtype: 'ehr-multianimalfiltertype',
            inputValue: EHR.panel.MultiAnimalFilterType.filterName,
            label: EHR.panel.MultiAnimalFilterType.label,
            aliasTable: {
                schemaName: 'study',
                queryName: 'alias',
                idColumn: 'Id',
                aliasColumn: 'alias'
            },
            addId: function (callback, panel) {
                var subjectArray = NIRC_EHR.Utils.splitIds(this.down('#subjArea').getValue()); // Don't lowercase Ids

                if (subjectArray.length > 0) {
                    subjectArray = Ext4.unique(subjectArray);
                    subjectArray.sort();
                }

                this.down('#subjArea').setValue(null);

                this.subjects = subjectArray;

                if(Ext4.isDefined(this.aliasTable)) {
                    this.aliases = {};
                    this.getAlias(subjectArray, callback, panel);
                } else {
                    callback.call(this);
                }
            },
        },{
            xtype: 'nirc_ehr-locationfiltertype',
            inputValue: EHR.panel.LocationFilterType.filterName,
            label: EHR.panel.LocationFilterType.label
        },{
            xtype: 'ldk-nofiltersfiltertype',
            inputValue: LDK.panel.NoFiltersFilterType.filterName,
            label: LDK.panel.NoFiltersFilterType.label
        });

        return allowedFilterTypes;
    }


});