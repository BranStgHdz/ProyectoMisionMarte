abstract class RastreadorObjetos{
    public abstract void ReductorInteresante(Game G);
}
// Clases colison para reducir los objetos interesantes
class ColisionInteresante extends RastreadorObjetos{
    @Override
    public void ReductorInteresante(Game G){
        G.setContObjetosInteresMap(G.getContObjetosInteresMap() - 1);
        G.setContObjetosInteresCapturados(G.getContObjetosInteresCapturados() + 1);
    }
}
class ColisionNoInteresante extends RastreadorObjetos{
    @Override
    public void ReductorInteresante(Game G){
        G.setContObjetosInteresMap(G.getContObjetosInteresMap() - 1);
    }
}