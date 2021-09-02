package com.workpodapp.workpod.otherclass;

/*public class UbicationFilter  extends Filter {
    List<Ubicacion> lstUbicacion;
    List<Ubicacion> lstUbicacionFilter;

    public UbicationFilter(List<Ubicacion> lstUbicacion, List<Ubicacion> lstUbicacionFilter) {
        this.lstUbicacion = lstUbicacion;
        this.lstUbicacionFilter = lstUbicacionFilter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        String filterString = constraint.toString().toLowerCase();
        FilterResults results = new FilterResults();

        final List<Ubicacion> list = lstUbicacion;

        int count = list.size();
        final ArrayList<Ubicacion> nlist = new ArrayList<>(count);

        Ubicacion filterableUbicacion ;

        for (int i = 0; i < count; i++) {
            filterableUbicacion = list.get(i);
            if (filterableUbicacion.getDireccion().toLongString().toLowerCase().contains(filterString)) {
                nlist.add(filterableUbicacion);
            }
        }

        results.values = nlist;
        results.count = nlist.size();

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        lstUbicacionFilter = (ArrayList<Ubicacion>) results.values;
        notifyDataSetChanged();
    }

}
*/