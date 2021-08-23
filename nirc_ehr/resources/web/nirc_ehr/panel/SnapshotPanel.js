Ext4.define('NIRC_EHR.panel.SnapshotPanel', {
    extend: 'EHR.panel.SnapshotPanel',
    alias: 'widget.nirc_ehr-snapshotpanel',

    appendDemographicsResults: function(toSet, row, id){
        if (!row){
            console.log('Id not found');
            return;
        }

        var animalId = row.getId() || id;
        if (!Ext4.isEmpty(animalId)){
            toSet['animalId'] = LABKEY.Utils.encodeHtml(id);
        }

        var status = row.getCalculatedStatus() || 'Unknown';
        toSet['calculated_status'] = '<span ' + (status.toLowerCase() !== 'alive' ? 'style="background-color:yellow"' : '') + '>'
                + LABKEY.Utils.encodeHtml(status) + '</span>';

        toSet['species'] = LABKEY.Utils.encodeHtml(row.getSpecies());
        toSet['geographic_origin'] = LABKEY.Utils.encodeHtml(row.getGeographicOrigin());
        toSet['gender'] = LABKEY.Utils.encodeHtml(row.getGender());
        toSet['age'] = LABKEY.Utils.encodeHtml(row.getAgeInYearsAndDays());

        var location;
        if (row.getActiveHousing() && row.getActiveHousing().length){
            var housingRow = row.getActiveHousing();
            location = '';
            if (!Ext4.isEmpty(row.getCurrentCage()))
                location +=  LABKEY.Utils.encodeHtml(row.getCurrentCage());

            if (location){
                if (this.showLocationDuration && housingRow.date){
                    var date = LDK.ConvertUtils.parseDate(housingRow.date);
                    if (date)
                        location += ' (' + Ext4.Date.format(date, LABKEY.extDefaultDateFormat) + ')';
                }
            }
        }

        toSet['location'] = location || 'No active housing';
    },

});